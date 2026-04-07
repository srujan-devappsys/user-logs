package com.example.myapplication.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import org.json.JSONObject;
public class Plugin {
    private static Plugin instance;
    protected Context context;
    private LogsPlugin config;
    private boolean isUploading =false;
    public Plugin(Context context,LogsPlugin config) {
        this.context = context.getApplicationContext();
        this.config =config;
        initNetworkListener();
    }
    public static Plugin init(Context context, LogsPlugin config) {
        if (instance == null) {
            instance = new Plugin(context,config);
        }
        return instance;
    }
    public void log(String message) {
        //String log = "Log: " + message + " | Time: " + System.currentTimeMillis();

        LocalStorage.savelog(context, message);
    }

    public void initNetworkListener() {
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(isAvailable -> {
            if(isAvailable)
            {
                Log.d("PLUGIN", "Internet Available ->uploading logs");
                uploadLogs();
            }
            else{
                Log.d("PLUGIN", "Internet lost ->storing logs");
                String log = "Connection failed";
                LocalStorage.savelog(context, log);
            }
        });

        android.content.IntentFilter filter = new android.content.IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }
    public void uploadLogs() {
        if (isUploading) {
            Log.d("PLUGIN", "Upload already in progress, skipping...");
            return;
        }

        isUploading = true;
        new Thread(() -> {
            try {
                String response = StsManager.getPresignedUrl(config);
                if (response == null) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> uploadLogs(), 5000);
                    return;
                }
                JSONObject json = new JSONObject(response);
                String presignedUrl = json.getString("url");
                String jsonData = LocalStorage.readLogs(context);
//                S3Uploader.uploadToS3(presignedUrl, jsonData);
//                Log.d("PLUGIN", "Logs uploaded successfully");
                boolean success = S3Uploader.uploadToS3(presignedUrl, jsonData);
                if (success) {
                    Log.d("PLUGIN", "Logs uploaded successfully");
                    LocalStorage.clearLogs(context);
                } else {
                    Log.d("PLUGIN", "Upload failed, retrying...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
            isUploading = false;
            }
            }).start();
    }
}
