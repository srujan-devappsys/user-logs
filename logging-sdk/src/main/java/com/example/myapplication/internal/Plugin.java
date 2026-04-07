package com.example.myapplication.internal;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Plugin {

    private static Plugin instance;

    private final Context context;
    private final LogsPlugin config;

    // 🔥 Threads (like grpc)
    private Handler logHandler;
    private Handler networkHandler;

    // 🔥 Counters
    private final AtomicInteger logCounter = new AtomicInteger(0);
    private final AtomicBoolean isUploading = new AtomicBoolean(false);

    private boolean isNetworkAvailable = false;

    private Plugin(Context context, LogsPlugin config) {
        this.context = context.getApplicationContext();
        this.config = config;

        initThreads();
        initNetworkListener();
    }

    // ✅ Builder style init
    public static Plugin init(Context context, LogsPlugin config) {
        if (instance == null) {
            instance = new Plugin(context, config);
        }
        return instance;
    }

    public static Plugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin not initialized");
        }
        return instance;
    }

    // 🔥 THREAD SETUP
    private void initThreads() {
        HandlerThread logThread = new HandlerThread("logThread");
        logThread.start();
        logHandler = new Handler(logThread.getLooper());

        HandlerThread networkThread = new HandlerThread("networkThread");
        networkThread.start();
        networkHandler = new Handler(networkThread.getLooper());
    }

    // 🔥 LOG METHOD (Grpc style)
//    public void log(String message) {
//        logHandler.post(() -> {
//
//            try {
//                JSONObject log = new JSONObject();
//                log.put("message", message);
//                log.put("timestamp", System.currentTimeMillis());
//                log.put("userId", config.getUserId());
//                log.put("project", config.getProject());
//
//                LocalStorage.savelog(context, log.toString());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // 🔁 Batch logic
//            if (logCounter.incrementAndGet() >= config.getBatchSize()) {
//                logCounter.set(0);
//
//                if (!isNetworkAvailable) return;
//
//                triggerUpload();
//            }
//        });
//    }
    public void log(int level, String message, Map<String, Object> properties) {
        logInternal(level, message, null, properties);
    }

    private void logInternal(int level, String message, String stackTrace, Map<String, Object> properties) {

        logHandler.post(() -> {

            try {
                JSONObject logJson = new JSONObject();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.getDefault()
                );
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                logJson.put("message", message);
                logJson.put("event_time", sdf.format(new Date()));
                logJson.put("level", level);
                logJson.put("display_name", android.os.Build.MODEL);
                logJson.put("os", "Android " + android.os.Build.VERSION.RELEASE);
                logJson.put("event_type","abc");

                if (stackTrace != null) {
                    logJson.put("stackTrace", stackTrace);
                }
                if (properties != null) {
                    JSONObject props = new JSONObject(properties);
                    logJson.put("properties", props);
                }
                LocalStorage.savelog(context, logJson.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (logCounter.incrementAndGet() >= config.getBatchSize()) {

                logCounter.set(0);

                if (!isNetworkAvailable) return;

                triggerUpload();
            }
        });
    }


    // 🔥 NETWORK LISTENER
    private void initNetworkListener() {

        NetworkChangeReceiver receiver = new NetworkChangeReceiver(isAvailable -> {

            isNetworkAvailable = isAvailable;

            if (isAvailable) {
                Log.d("PLUGIN", "Internet Available → flush logs");
                triggerUpload();
            } else {
                Log.d("PLUGIN", "Internet lost → storing logs");
            }
        });

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }

    // 🔥 TRIGGER UPLOAD (safe)
    private void triggerUpload() {
        if (!isUploading.compareAndSet(false, true)) {
            Log.d("PLUGIN", "Upload already running");
            return;
        }

        networkHandler.post(() -> {
            try {
                uploadLogsInternal();
            } finally {
                isUploading.set(false);
            }
        });
    }

    // 🔥 MAIN UPLOAD LOGIC
    private void uploadLogsInternal() {

        try {
            String response = StsManager.getPresignedUrl(config);

            if (response == null) {
                retryUpload();
                return;
            }

            JSONObject json = new JSONObject(response);
            String presignedUrl = json.getString("url");

            String logs = LocalStorage.readLogs(context);

            if (logs == null || logs.isEmpty()) {
                Log.d("PLUGIN", "No logs to upload");
                return;
            }

            boolean success = S3Uploader.uploadToS3(presignedUrl, logs);

            if (success) {
                Log.d("PLUGIN", "Logs uploaded successfully");
                LocalStorage.clearLogs(context);
            } else {
                Log.d("PLUGIN", "Upload failed");
                retryUpload();
            }

        } catch (Exception e) {
            e.printStackTrace();
            retryUpload();
        }
    }

    // 🔁 RETRY
    private void retryUpload() {
        new Handler(context.getMainLooper()).postDelayed(this::triggerUpload, 5000);
    }
}