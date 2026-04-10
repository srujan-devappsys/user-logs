//package com.example.myapplication.internal;
//
//public class utitlity {
//}
//package com.survey.util;
//
//import static com.survey.util.Constants.APP_SIGNATURE;
//import static com.survey.util.Constants.PACKAGE_NAME;
//
//import static android.content.Context.ACTIVITY_SERVICE;
//import static androidx.core.content.ContextCompat.getSystemService;
//
//import static org.apache.commons.text.WordUtils.capitalize;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//
//import androidx.annotation.RequiresApi;
//import androidx.exifinterface.media.ExifInterface;
//
//import android.net.ConnectivityManager;
//import android.net.LinkProperties;
//import android.net.Network;
//import android.net.NetworkCapabilities;
//import android.net.ProxyInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Debug;
//import android.os.Environment;
//import android.os.Handler;
//import android.provider.AlarmClock;
//import android.provider.Settings;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.FileProvider;
//import androidx.lifecycle.LifecycleObserver;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewTreeLifecycleOwner;
//import androidx.work.Constraints;
//import androidx.work.Data;
//import androidx.work.NetworkType;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkInfo;
//import androidx.work.WorkManager;
//import androidx.work.WorkRequest;
//
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.firebase.crashlytics.FirebaseCrashlytics;
//import com.google.firebase.crashlytics.internal.common.CommonUtils;
//import com.survey.ApplicationClass;
//import com.styraPro.BuildConfig;
//import com.styraPro.R;
//import com.survey.activity.MainActivity;
//import com.survey.data.DraftSettings;
//import com.survey.data.LocationData;
//import com.survey.data.Project;
//import com.survey.database.DataSource;
//import com.survey.s3.services.SurveyUploadService;
//import com.survey.s3.workers.SurveyInitiateWorker;
//import com.survey.service.AmplitudeHelper;
//import com.survey.workers.DeletedNodeSyncWorker;
//import com.survey.workers.LogsSyncWorker;
//import com.survey.workers.NotificationWorker;
//import com.survey.workers.SurveyUploadWorker;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.Proxy;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.channels.FileChannel;
//import java.nio.charset.StandardCharsets;
//import java.text.DateFormat;
//import java.security.MessageDigest;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Objects;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.HttpsURLConnection;
//
//public class Utility {
//
//    public static final Comparator locationSorting = new Comparator<LocationData>() {
//        @Override
//        public int compare(LocationData locationData1, LocationData locationData2) {
//            return String.CASE_INSENSITIVE_ORDER.compare(locationData1.getLocation(),
//                    locationData2.getLocation());
//        }
//    };
//
//    public static final Comparator projectSorting = new Comparator<Project>() {
//        @Override
//        public int compare(Project project1, Project project2) {
//            return String.CASE_INSENSITIVE_ORDER.compare(project1.getProName(),
//                    project2.getProName());
//        }
//    };
//
//    private static final String DATE_FORMAT = "dd/MM/yyyy";
//    private static final String DATE_FILE_FORMAT = "dd-MM-yyyy";
//    private static final String TIME_FORMAT = "hh:mm:ss";
//    private static final String TIME_FORMAT_FOR_DB = "hh_mm_ss";
//    private static final String TIME_FORMAT_24 = "HH:mm:ss";
//    private static final String TIME_FORMAT_UNIQUE = "HHmmssSSS";
//    private static final String CAPTURE_DATE = "yyyyMMdd_HHmmss";
//
//    public static void hideKeyBoard(Context context, View view) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static int getDeviceWidthAndHeight(Context context, int dimType) {
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        int devWidth = displayMetrics.widthPixels;
//        int devHeight = displayMetrics.heightPixels;
//        switch (dimType) {
//            case Constants.DIM_TYPE_DEVICE_HEIGHT:
//                return devHeight;
//            case Constants.DIM_TYPE_DEVICE_WIDTH:
//                return devWidth;
//        }
//        return 0;
//    }
//
//    /**
//     * Gets the state of Airplane Mode.
//     *
//     * @param context
//     * @return true if enabled.
//     */
//    public static boolean isAirplaneModeOn(Context context) {
//
//        return Settings.System.getInt(context.getContentResolver(),
//                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
//
//    }
//
//    public static String getCurrentDate() {
//        return new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(new Date());
//    }
//
//    public static String getCurrentDateFileFormat() {
//        return new SimpleDateFormat(DATE_FILE_FORMAT, Locale.ENGLISH).format(new Date());
//    }
//
//    public static String getCurrentTime() {
//        return new SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH).format(new Date());
//    }
//
//    public static String getCurrentTimeForDBFile() {
//        return new SimpleDateFormat(TIME_FORMAT_FOR_DB, Locale.ENGLISH).format(new Date());
//    }
//
//    public static String getCurrentTime24() {
//        return new SimpleDateFormat(TIME_FORMAT_24, Locale.ENGLISH).format(new Date());
//    }
//
//    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
//        FileChannel fromChannel;
//        FileChannel toChannel;
//        try {
//            fromChannel = fromFile.getChannel();
//            toChannel = toFile.getChannel();
//            fromChannel.transferTo(0, fromChannel.size(), toChannel);
//            fromChannel.close();
//            toChannel.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String getVersionName(Context context) {
//        PackageManager packageManager = context.getPackageManager();
//        String versionName = "";
//        try {
//            // Get the package information
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//
//            // Retrieve the version information
//            versionName = packageInfo.versionName;
//            int versionCode = packageInfo.versionCode;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return versionName;
//    }
//
//    public static int getVersionCode(Context context) {
//        PackageManager packageManager = context.getPackageManager();
//        int versionCode = 0;
//        try {
//            // Get the package information
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//
//            // Retrieve the version information
//            versionCode = packageInfo.versionCode;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return versionCode;
//    }
//
//    public static String getDeviceId(Context context) {
//        return Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//    }
//
//    public static String getTimeStamp() {
//        return new SimpleDateFormat(CAPTURE_DATE, Locale.ENGLISH).format(new Date());
//    }
//
//    public static String milliSecondsToTimer(long milliseconds) {
//        String finalTimerString = Constants.EMPTY_STRING;
//        String secondsString;
//        int hours = (int) (milliseconds / (Constants.MILLISECONDS * Constants.SECONDS * Constants.SECONDS));
//        int minutes = (int) (milliseconds % (Constants.MILLISECONDS * Constants.SECONDS * Constants.SECONDS))
//                / (Constants.MILLISECONDS * Constants.SECONDS);
//        int seconds = (int) ((milliseconds % (Constants.MILLISECONDS * Constants.SECONDS * Constants.SECONDS))
//                % (Constants.MILLISECONDS * Constants.SECONDS) / Constants.MILLISECONDS);
//        if (hours > 0) {
//            finalTimerString = hours + ":";
//        }
//        if (seconds < 10) {
//            secondsString = "0" + seconds;
//        } else {
//            secondsString = "" + seconds;
//        }
//        finalTimerString = finalTimerString + minutes + ":" + secondsString;
//        return finalTimerString;
//    }
//
//    public static int getProgressPercentage(long currentDuration, long totalDuration) {
//        Double percentage;
//        long currentSeconds = (int) (currentDuration / Constants.MILLISECONDS);
//        long totalSeconds = (int) (totalDuration / Constants.MILLISECONDS);
//        percentage = (((double) currentSeconds) / totalSeconds) * 100;
//        return percentage.intValue();
//    }
//
//    public static int progressToTimer(int progress, int totalDuration) {
//        int currentDuration;
//        totalDuration = totalDuration / Constants.MILLISECONDS;
//        currentDuration = (int) ((((double) progress) / 100) * totalDuration);
//        return currentDuration * Constants.MILLISECONDS;
//    }
//
//    /**
//     * fix the image orientation issue in some devices
//     */
//    public static Bitmap correctOrientation(Bitmap bitmap, String path) {
//        try {
//            ExifInterface exif = new ExifInterface(path);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//            Matrix matrix = new Matrix();
//            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//                matrix.postRotate(90);
//            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//                matrix.postRotate(180);
//            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//                matrix.postRotate(270);
//            }
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    static public boolean setGeoTag(Context context, String path, Location geoTag) {
//        if (geoTag != null) {
//            try {
//                ExifInterface exif = new ExifInterface(path);
//                ExecutorService es2 = Executors.newCachedThreadPool();
//                es2.execute(new Runnable() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void run() {
//                        try {
//                            StringBuilder response = new StringBuilder();
//                            String latLong = geoTag.getLatitude() + "," + geoTag.getLongitude();
//                            String apiEndpoint = "https://plus.codes/api?address=" + latLong;
//
//                            // Create a URL object from the API endpoint
//                            URL url = new URL(apiEndpoint);
//
//
//                            // Open a connection to the URL
//                            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//
//                            // Set the request method to POST
//                            connection.setRequestMethod("POST");
//
//                            // Set the request headers
//                            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                            connection.setRequestProperty("Accept", "application/json");
//                            connection.setRequestProperty(Constants.TOKEN_NAME, PreferenceUtils.getAuthToken(ApplicationClass.getSurveyContext()));
//
//
//                            // Enable output (i.e., sending data to the server)
//                            connection.setDoOutput(true);
//
//                            // Check if the request was successful (response code 200 indicates success)
//                            int responseCode = connection.getResponseCode();
//
//                            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                                // Read the response from the server
//                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                                    String line;
//
//                                    while ((line = reader.readLine()) != null) {
//                                        response.append(line);
//                                    }
//                                }
//                                JSONObject responseObject = new JSONObject(response.toString());
//                                String plusCodeString = responseObject.getString("plus_code");
//                                JSONObject plusCodeObj = new JSONObject(plusCodeString);
//                                JSONObject globalCodeObject = new JSONObject();
//                                globalCodeObject.put("global_code", plusCodeObj.getString("global_code"));
//                                exif.setAttribute(ExifInterface.TAG_MAKER_NOTE, globalCodeObject.toString());
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                es2.shutdown();
//                try {
//                    boolean finished = es2.awaitTermination(4, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                double lat = geoTag.getLatitude();
//                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat > 0 ? "N" : "S");
//                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, DMS(lat));
//
//                // Longitude
//                double lon = geoTag.getLongitude();
//                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon > 0 ? "E" : "W");
//                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, DMS(lon));
//
//                // Date/time
////                Date date = new Date(geoTag.getTime());
//                Date date = Calendar.getInstance().getTime();
//
//                exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, new SimpleDateFormat("dd-MM-yyyy").format(date));
//                exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, new SimpleDateFormat("HH:mm:ss").format(date));
//                String dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
//                exif.setAttribute(ExifInterface.TAG_DATETIME, dateString);
//                exif.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, dateString);
//                // Altitude
//                if (geoTag.hasAltitude()) {
//                    double altitude = geoTag.getAltitude();
//                    exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, altitude > 0 ? "0" : "1");
//                    exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, String.valueOf(Math.abs(altitude)));
//                }
//
//                // Speed
//                if (geoTag.hasSpeed()) {
//                    exif.setAttribute("GPSSpeedRef", "K"); // Km/h
//                    exif.setAttribute("GPSSpeed", String.valueOf(geoTag.getSpeed() * 3600 / 1000));
//                }
//                final String[] address = {""};
//                ExecutorService es3 = Executors.newCachedThreadPool();
//                es3.execute(new Runnable() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void run() {
//                        try {
//                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
//                            address[0] = addresses.get(0).getAddressLine(0);
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                });
//
//                es3.shutdown();
//                try {
//                    boolean finished = es3.awaitTermination(4, TimeUnit.SECONDS);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                String area = "Date: " + dateString + "=" + "Latitude: " + lat + "=Longitude: " + lon;
//                area = area + "=" + address[0];
//                exif.setAttribute(ExifInterface.TAG_MAKE, area);
//                exif.saveAttributes();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
//
//    private static String DMS(double x) {
//        int d = (int) Math.abs(x);
//        int m = (int) Math.abs((x % 1) * 60);
//        int s = (int) Math.abs((((x % 1) * 60) % 1) * 60);
//        return String.format("%d/1,%d/1,%d/1", d, m, s);
//    }
//
//    public static Bitmap getBitmap(String path, Context context) {
//
//        Bitmap bitmap = null;
//        FileInputStream fileInputStream = null;
//        try {
//            File file = new File(path);
//            fileInputStream = new FileInputStream(file);
//            if (file.exists()) {
//                bitmap = BitmapFactory.decodeStream(fileInputStream);
//            }
//
//        } catch (FileNotFoundException e) {
//            Log.e("FileToBitmapConverter", "File not found: " + e.getMessage());
//        } finally {
//            try {
//                if (fileInputStream != null)
//                    fileInputStream.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return bitmap;
//    }
//
//    public static String getUrl(LatLng origin, LatLng dest) {
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        String sensor = "sensor=false";
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&units=metric&mode=walking";
//        String output = "json";
//        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//    }
//
//    public static int getUniqueId() {
//        try {
//            return Integer.parseInt(new SimpleDateFormat(TIME_FORMAT_UNIQUE,
//                    Locale.ENGLISH).format(new Date()));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    public static String imageDestinationPath(String imageName) {
//        File appDirectory = new File(Environment.getExternalStorageDirectory(), Constants.SURVEY_EXTERNAL_DIR);
//        if (!appDirectory.exists()) {
//            appDirectory.mkdirs();
//        }
//
//        File logo = new File(appDirectory, imageName);
//        return logo.getAbsolutePath();
//    }
//
//    public static boolean checkImageFileExists(String logoName) {
//        ViewUtils.showLog(logoName);
//        boolean retVal = false;
//        File fileWithinMyDir = new File(Utilityy.imageDestinationPath(logoName));
//        if (fileWithinMyDir.exists()
//                && BitmapFactory.decodeFile(Utilityy.imageDestinationPath(logoName)) != null) {
//            retVal = true;
//        }
//        return retVal;
//    }
//
//    public static boolean downloadFile(String logoUrl) {
//        try {
//            File file = new File(Utilityy.imageDestinationPath(new File(logoUrl).getName()));
//            //Stream used for reading the data from the internet
//            URL url = new URL(logoUrl);
//            URLConnection urlConnection = url.openConnection();
//            int length = urlConnection.getContentLength();
//            if (length != -1 && file.length() < length) {
//                FileOutputStream fileOutput = new FileOutputStream(file);
//                InputStream inputStream = urlConnection.getInputStream();
//                //create a buffer...
//                byte[] buffer = new byte[1024];
//                int bufferLength;
//                while ((bufferLength = inputStream.read(buffer)) > 0) {
//                    fileOutput.write(buffer, 0, bufferLength);
//                }
//                fileOutput.close();
//            }
//            return true;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static void closeBT(OutputStream outputStream, InputStream inputStream, BluetoothSocket bluetoothSocket) {
//        try {
//            if (outputStream != null) {
//                outputStream.close();
//                inputStream.close();
//                bluetoothSocket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static List<BluetoothDevice> getPairedDevices(Context context) {
//        List<BluetoothDevice> bluetoothDeviceList = null;
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            ViewUtils.showToast(context, context.getResources().getString(R.string.no_bluetooth_adapter));
//            return null;
//        }
//
//        if (bluetoothAdapter != null) {
////            bluetoothAdapter.cancelDiscovery();
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    ((Activity) context).requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 111);
//                }
//            }
//            if (!bluetoothAdapter.isEnabled()) {
//                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                context.startActivity(enableBluetooth);
//            } else {
//                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//                bluetoothDeviceList = new ArrayList<>(pairedDevices);
//            }
//        }
//
//        if (bluetoothDeviceList == null || bluetoothDeviceList.size() == 0) {
//            ViewUtils.showToast(context, context.getResources().getString(R.string.check_printer));
//        }
//        return bluetoothDeviceList;
//    }
//
//    public static UUID getUuid() {
//        return UUID.fromString(Constants.USERID);
//    }
//
//    public static String validateHostDevice(Context context) {
//        if (!debugMode()) {
//            if (isADBEnabled(context)) {
//                return context.getString(R.string.adb_warning);
//            }
//        }
//        if (!debugMode()) {
//            if (isDeveloperOptionEnabled(context)) {
//                return context.getString(R.string.developer_option_warning);
//            }
//        }
//        if (isProxyOrVPNEnabled(context)) {
//            return context.getString(R.string.proxy_vpn_warning);
//        }
//        if (!isAppSignatureValid(context)) {
//            return context.getString(R.string.tempered_app_warning);
//        }
//        if (isEmulator()) {
//            return context.getString(R.string.emulator_warning);
//        }
//        if (isDeviceRooted()) {
//            return context.getString(R.string.rooted_device_warning);
//        }
//        return null; //all good
//    }
//
//    public static boolean isDeviceRooted() {
//        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4() || checkRootMethod5() || checkRootMethod6() || checkRootMethod7();
//    }
//
//    private static boolean checkRootMethod1() {
//        String buildTags = android.os.Build.TAGS;
//        return buildTags != null && buildTags.contains("test-keys");
//    }
//
//    private static boolean checkRootMethod2() {
//        String[] paths = {
//                "/system/app/Superuser.apk",
//                "/sbin/su",
//                "/system/bin/su",
//                "/system/xbin/su",
//                "/data/local/xbin/su",
//                "/data/local/bin/su",
//                "/system/sd/xbin/su",
//                "/system/bin/failsafe/su",
//                "/data/local/su",
//                "/su/bin/su"};
//        for (String path : paths) {
//            if (new File(path).exists()) return true;
//        }
//        return false;
//    }
//
//    private static boolean checkRootMethod3() {
//        Process process = null;
//        try {
//            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            return in.readLine() != null;
//        } catch (Throwable t) {
//            return false;
//        } finally {
//            if (process != null) process.destroy();
//        }
//    }
//
//    private static boolean checkRootMethod4() {
//        final String[] knownRootAppsPackages = {
//                "com.noshufou.android.su",
//                "com.noshufou.android.su.elite",
//                "eu.chainfire.supersu",
//                "com.koushikdutta.superuser",
//                "com.thirdparty.superuser",
//                "com.yellowes.su",
//                "com.topjohnwu.magisk",
//                "com.kingroot.kinguser",
//                "com.kingo.root",
//                "com.smedialink.oneclickroot",
//                "com.zhiqupk.root.global",
//                "com.alephzain.framaroot"};
//        boolean result = false;
//        for (String string1 : knownRootAppsPackages) {
//            if (isPackageInstalled(string1, ApplicationClass.getSurveyContext())) {
//                result = true;
//                break;
//            }
//        }
//        return result;
//    }
//
//    private static boolean isPackageInstalled(String packagename, Context context) {
//        PackageManager pm = context.getPackageManager();
//        try {
//            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }
//
//
//    public static boolean checkRootMethod5() {
//        final String[] knownSUPaths = {
//                "/system/bin/su",
//                "/system/xbin/su",
//                "/sbin/su",
//                "/system/su",
//                "/system/bin/.ext/.su",
//                "/system/usr/we-need-root/su-backup",
//                "/system/xbin/mu"};
//        boolean result = false;
//        for (String string1 : knownSUPaths) {
//            File f = new File(string1);
//            boolean fileExists = f.exists();
//            if (fileExists) {
//                result = true;
//            }
//        }
//        return result;
//    }
//
//    public static boolean checkRootMethod6() {
//        final String[] knownRootCloakingPackages = {
//                "com.devadvance.rootcloak",
//                "com.devadvance.rootcloakplus",
//                "de.robv.android.xposed.installer",
//                "com.saurik.substrate",
//                "com.zachspong.temprootremovejb",
//                "com.amphoras.hidemyroot",
//                "com.amphoras.hidemyrootadfree",
//                "com.formyhm.hiderootPremium",
//                "com.formyhm.hideroot"};
//        ArrayList<String> packages = new ArrayList<>(Arrays.asList(knownRootCloakingPackages));
//        return isAnyPackageFromListInstalled(packages);
//    }
//
//    public static boolean isAnyPackageFromListInstalled(List<String> packages) {
//        boolean result = false;
//        PackageManager pm = ApplicationClass.getSurveyContext().getPackageManager();
//        for (String packageName : packages) {
//            try {
//                pm.getPackageInfo(packageName, 0);
//                result = true;
//            } catch (
//                    PackageManager.NameNotFoundException e) {           // Exception thrown, package is not installed into the system
//            }
//        }
//        return result;
//    }
//
//
//    public static boolean checkRootMethod7() {
//        if (isRootAvailable()) {
//            Process process = null;
//            try {
//                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
//                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String output = in.readLine();
//                if (output != null && output.toLowerCase().contains("uid=0"))
//                    return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (process != null)
//                    process.destroy();
//            }
//        }
//
//        return false;
//    }
//
//    public static boolean isRootAvailable() {
//        for (String pathDir : System.getenv("PATH").split(":")) {
//            if (new File(pathDir, "su").exists()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean isEmulator() {
//
////        String radioVersion = android.os.Build.getRadioVersion();
////        return radioVersion == null || radioVersion.isEmpty() || radioVersion.equals("1.0.0.0");
//        // 7201 -  the above code does not work with tabs that do not have cellular, the below solution was suggested
//        final String GOLDFISH = "goldfish";
//        final String RANCHU = "ranchu";
//        final String SDK = "sdk";
////        return Build.PRODUCT.contains(SDK)
////                || Build.HARDWARE.contains(GOLDFISH)
////                || Build.HARDWARE.contains(RANCHU);
//
//        return Objects.equals(Build.MANUFACTURER, "Google") && Objects.equals(Build.BRAND, "google") &&
//                ((Build.FINGERPRINT.startsWith("google/sdk_gphone_")
//                        && Build.FINGERPRINT.endsWith(":user/release-keys")
//                        && Build.PRODUCT.startsWith("sdk_gphone_")
//                        && Build.MODEL.startsWith("sdk_gphone_"))
//                        //alternative
//                        || (Build.FINGERPRINT.startsWith("google/sdk_gphone64_") && (Build.FINGERPRINT.endsWith(":userdebug/dev-keys")
//                        || (Build.FINGERPRINT.endsWith(":user/release-keys")) && Build.PRODUCT.startsWith("sdk_gphone64_")
//                        && Build.MODEL.startsWith("sdk_gphone64_")))
//                        //Google Play Games emulator https://play.google.com/googleplaygames https://developer.android.com/games/playgames/emulator#other-downloads
//                        || (Objects.equals(Build.MODEL, "HPE device") &&
//                        Build.FINGERPRINT.startsWith("google/kiwi_") && Build.FINGERPRINT.endsWith(":user/release-keys")
//                        && Objects.equals(Build.BOARD, "kiwi") && Build.PRODUCT.startsWith("kiwi_"))
//                )
//                //
//                || Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")
//                //bluestacks
//                || "QC_Reference_Phone" == Build.BOARD && !"Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)
//                //bluestacks
//                || Build.MANUFACTURER.contains("Genymotion")
//                || Build.HOST.startsWith("Build")
//                //MSI App Player
//                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
//                || Build.PRODUCT == "google_sdk"
//                || Build.PRODUCT.contains(SDK)
//                || Build.HARDWARE.contains(GOLDFISH)
//                || Build.HARDWARE.contains(RANCHU);
//
//    }
//
//    public static boolean isProxyOrVPNEnabled(Context context) {
//        boolean isProxyEnabled = false;
//        boolean isVpnEnabled = false;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
//            Network currentNetwork = connectivityManager.getActiveNetwork();
//            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
//            LinkProperties linkProperties = connectivityManager.getLinkProperties(currentNetwork);
//            if (linkProperties != null) {
//                ProxyInfo proxyInfo = linkProperties.getHttpProxy();
//                if (proxyInfo != null) {
//                    if (proxyInfo.getHost() != null) {
//                        isProxyEnabled = true;
//                    }
//                }
//            }
//
//            //just a fallback method, just in case
//            ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            ProxyInfo proxy = connectivityManager2.getDefaultProxy();
//            if (proxy != null) {
//                isProxyEnabled = true;
//            }
//
//            if (caps != null) {
//                isVpnEnabled = !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN);
//            }
//        }
//        return isProxyEnabled || isVpnEnabled;
//    }
//
//
//    public static boolean isAppDebuggable(Context context) {
//        boolean isDebuggable = (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
//        boolean isDebuggerConnected = Debug.isDebuggerConnected();
//        return isDebuggable || isDebuggerConnected;
//    }
//
//    public static boolean isDeveloperOptionEnabled(Context context) {
//        //;dev option not enalbed
//        return Settings.Secure.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1;
//    }
//
//    public static boolean isADBEnabled(Context context) {
//        //;debugging does not enabled
//        return Settings.Secure.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0) == 1;
//    }
//
//
//    public static boolean isAppSignatureValid(Context context) {
//
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            boolean isSingatureValid = false;
//            boolean isPackgeNameValid = false;
//            boolean isAppNameValid = false;
//            for (Signature signature : packageInfo.signatures) {
//
//                byte[] signatureBytes = signature.toByteArray();
//
//                MessageDigest md = MessageDigest.getInstance("SHA");
//
//                md.update(signature.toByteArray());
//
//                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//
////                Log.d("REMOVE_ME", "Include this string as a value for SIGNATURE:" + currentSignature);
//                //compare signatures
//                if (APP_SIGNATURE.trim().equals(currentSignature.trim())) {
//                    isSingatureValid = true;
//                }
//            }
//
//            if (packageInfo.packageName.equals(PACKAGE_NAME)) {
//                isPackgeNameValid = true;
//            }
//
//            ApplicationInfo applicationInfo = context.getApplicationInfo();
//            int stringId = applicationInfo.labelRes;
//            String appName = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
//
//            if (appName.equals(context.getString(R.string.app_name))) {
//                isAppNameValid = true;
//            }
//
//            return isSingatureValid && isPackgeNameValid && isAppNameValid;
//
//
//        } catch (Exception e) {
//            //assumes an issue in checking signature., but we let the caller decide on what to do.
//        }
//        return false;
//
//    }
//
//    public static boolean debugMode() {
//        return BuildConfig.DEBUG;
//    }
//
//    private static Uri getUriForFile(Context context, File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            try {
//                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
//            } catch (IllegalArgumentException e) {
//                throw new SecurityException();
//            }
//        } else {
//            return Uri.fromFile(file);
//        }
//    }
//
//    public static String join(List<Integer> iList, String SEPARATOR) {
//        if (iList == null || iList.size() == 0) return "";
//
//        if (SEPARATOR == null) SEPARATOR = "";
//
//        StringBuilder sb = new StringBuilder();
//        for (int i : iList) {
//            sb.append(i).append(SEPARATOR);
//        }
//        String joinedStr = sb.toString();
//
//        joinedStr = joinedStr.substring(0, joinedStr.length() - SEPARATOR.length());
//        return joinedStr;
//    }
//
//    public JSONArray cur2Json(Cursor cursor) {
//
//        JSONArray resultSet = new JSONArray();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            int totalColumn = cursor.getColumnCount();
//            JSONObject rowObject = new JSONObject();
//            for (int i = 0; i < totalColumn; i++) {
//                if (cursor.getColumnName(i) != null) {
//                    try {
//                        rowObject.put(cursor.getColumnName(i),
//                                cursor.getString(i));
//                    } catch (Exception e) {
//                        Log.d("TAG", e.getMessage());
//                    }
//                }
//            }
//            resultSet.put(rowObject);
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//        return resultSet;
//
//    }
//
//    public static JSONArray cv2Json(ContentValues cv) {
//        try {
//            JSONArray resultSet = new JSONArray();
//            for (String ks : cv.keySet()) {
//                Object value = cv.get(ks);
//                JSONObject rowObject = new JSONObject();
//                rowObject.put(ks, value);
//                resultSet.put(rowObject);
//            }
//            return resultSet;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return new JSONArray();
//        }
//    }
//
////    public static void addFirebaseLog(String logMessege) {
////        FirebaseCrashlytics.getInstance().log(logMessege);
////        try {
////            throw new Exception("---New Exception--");
////        } catch (Exception e) {
////            FirebaseCrashlytics.getInstance().recordException(e);
////        }
////    }
//
//    public static void startDraftNotifications() {
//        DraftSettings draftSettings = PreferenceUtils.getDraftSettings(ApplicationClass.getSurveyContext());
//        if (draftSettings != null) {
//            int notify = draftSettings.getNotify();
//            int draftSurveyCount = ApplicationClass.getDataSource().getDraftSurveyCount();
//            int minDraftcount = draftSettings.getMinDraftCount();
//            if (notify == 1 && draftSurveyCount >= minDraftcount) {
//                PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(
//                        NotificationWorker.class, draftSettings.getNotifyFreq(), TimeUnit.MINUTES)
//                        .addTag(NotificationWorker.WORK_TAG)
//                        .build();
//
//                WorkManager.getInstance(ApplicationClass.getSurveyContext()).cancelAllWorkByTag(NotificationWorker.WORK_TAG);
//
//                WorkManager.getInstance(ApplicationClass.getSurveyContext()).enqueue(notificationWork);
//            }
//        }
//    }
//
//
//    public static boolean isCurrentTimeBetween(String fromTime, String toTime) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            // Get the current time in Indian GMT
//            LocalTime currentTime = LocalTime.now();
//
//            // Parse the formTime and toTime strings into LocalTime objects
//            LocalTime formTimeLocal = null;
//
//            formTimeLocal = LocalTime.parse(fromTime);
//
//            LocalTime toTimeLocal = LocalTime.parse(toTime);
//
//            // Check if the current time is between the formTime and toTime times
//            return currentTime.isAfter(formTimeLocal) && currentTime.isBefore(toTimeLocal);
//        }
//        return false;
//    }
//
//    public static boolean isValidURL(String urlString) {
//        try {
//            new URL(urlString);
//            return true;
//        } catch (MalformedURLException e) {
//            return false;
//        }
//    }
//
//    public static String getServerPathFromUrl(String urlString) {
//        try {
//            URL url = new URL(urlString);
//            String path = url.getPath();
//            String folderDir = PreferenceUtils.getProjectId(ApplicationClass.getSurveyContext()) + "/images/";
//            int index = path.indexOf(folderDir);
//            if (index != -1) {
//                return path.substring(index + folderDir.length());
//            }
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//
//    public static void startLogsSyncWorker() {
//
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
////Periodic worker
////        WorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(LogsSyncWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
////                .setConstraints(constraints)
////                .addTag(LogsSyncWorker.WORK_TAG)
////                .build();
//
//        int logFrequency = PreferenceUtils.getLogFrequency(ApplicationClass.getSurveyContext());
//        WorkRequest myWorkRequest;
//        if (logFrequency == 0) {
//            //One time worker
//            myWorkRequest = new OneTimeWorkRequest.Builder(LogsSyncWorker.class)
//                    .setConstraints(constraints)
//                    .addTag(LogsSyncWorker.WORK_TAG)
//                    .build();
//
//        } else {
//            myWorkRequest = new PeriodicWorkRequest.Builder(
//                    LogsSyncWorker.class,
//                    logFrequency, TimeUnit.MINUTES
//            )
//                    .setConstraints(constraints)
//                    .addTag(LogsSyncWorker.WORK_TAG)
//                    .build();
//
//        }
//        WorkManager.getInstance(ApplicationClass.getSurveyContext()).cancelAllWorkByTag(LogsSyncWorker.WORK_TAG);
//        WorkManager.getInstance(ApplicationClass.getSurveyContext()).enqueue(myWorkRequest);
//
//
//        PeriodicWorkRequest workRequest1 = new PeriodicWorkRequest.Builder(SurveyInitiateWorker.class, 1, TimeUnit.HOURS)
//                .addTag(SurveyInitiateWorker.WORK_TAG).setConstraints(constraints)
//                .build();
//        WorkManager.getInstance(ApplicationClass.getSurveyContext()).cancelAllWorkByTag(SurveyInitiateWorker.WORK_TAG);
//
//        WorkManager.getInstance(ApplicationClass.getSurveyContext()).enqueue(workRequest1);
//
////        WorkManager.getInstance(ApplicationClass.getSurveyContext()).getWorkInfoByIdLiveData(myWorkRequest.getId())
////                .observeForever(new Observer<WorkInfo>() {
////                    @Override
////                    public void onChanged(WorkInfo workInfo) {
////                        Log.e("worker status", workInfo.getState().toString() );
////                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
////                            Log.e("TAG", "startLogsSyncWorker: ");
////
////                            //remove observer after sucess
////                            WorkManager.getInstance(ApplicationClass.getSurveyContext())
////                                    .getWorkInfoByIdLiveData(myWorkRequest.getId())
////                                    .removeObserver(this);
////                        }
////                    }
////                });
//    }
//
//
//    public static void syncDeletedNode(int nodeId) {
//
//        Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//
//        Data nodeInfo = new Data.Builder()
//                .putInt(DeletedNodeSyncWorker.NODE_ID, nodeId)
//                .build();
//
//        WorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(DeletedNodeSyncWorker.class)
//                .setConstraints(constraints)
//                .addTag(DeletedNodeSyncWorker.WORK_TAG)
//                .setInputData(nodeInfo)
//                .build();
//
//
//        WorkManager.getInstance(ApplicationClass.getSurveyContext()).enqueue(myWorkRequest);
//
//
//    }
//
//    public static String getCurrentDateTime() {
//        Date currentTime = Calendar.getInstance().getTime();
//        final DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = isoFormat.format(currentTime.getTime());
//        return formattedDate;
//    }
//
//    //Device Information
//    public static String getAndroidVersion() {
//        return String.valueOf(Build.VERSION.RELEASE);
//    }
//
//    public static String getAndroidSDKVersion() {
//        return String.valueOf(Build.VERSION.SDK_INT);
//    }
//
//
//    @SuppressLint("HardwareIds")
//    // Using Build.SERIAL is discouraged on Android 10+ due to privacy concerns
//    public static String getProcessorInfo() {
//        // Limited information available through public APIs
//        String hardware = Build.HARDWARE;
//        return hardware;
//    }
//
//    public static String getModelName() {
//        return Build.MODEL;
//    }
//
//    public static String getAvailableMemory(Context context) {
//        try {
//            ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
//            actManager.getMemoryInfo(memInfo);
//            long availableRAM = memInfo.availMem / (1024 * 1024 * 1024);
//            return String.valueOf(availableRAM); // Convert to GB
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Failed to get RAM info";
//    }
//
//    public static String getTotalMemory(Context context) {
//        try {
//            ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
//            actManager.getMemoryInfo(memInfo);
//            return String.valueOf(memInfo.totalMem / (1024 * 1024 * 1024)); // Convert to GB
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Failed to get RAM info";
//    }
//
//    /* Checks if external storage is available for read and write */
//    public static boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        return Environment.MEDIA_MOUNTED.equals(state);
//    }
//
//    /* Checks if external storage is available to at least read */
//    public static boolean isExternalStorageReadable() {
//        String state = Environment.getExternalStorageState();
//        return Environment.MEDIA_MOUNTED.equals(state) ||
//                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
//    }
//
//    public static String fileSizeInMB(long length) {
//        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//        long fileSizeInKB = length / 1024;
//        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//        long fileSizeInMB = fileSizeInKB / 1024;
//        return String.valueOf(fileSizeInMB);
//    }
//
//    public static void logEvent(String event, String message, boolean localLog, boolean amplitude, boolean dblog, DataSource dataSource) {
//        if (localLog) {
//            Log.d(event + " : ", message);
//        }
//        if (dblog) {
//            if (dataSource != null)
//                dataSource.addLogDump(event + " : " + message, 1);
//        }
//        if (amplitude) {
//            AmplitudeHelper.logEventString(event, message);
//        }
//
//    }
//
//    public static boolean isAppInForeground(Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//
//        if (appProcesses == null) {
//            return false;
//        }
//
//        final String packageName = context.getPackageName();
//
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
//                    appProcess.processName.equals(packageName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static Location getBestLocation(LocationResult locationResult) {
//        Location bestLocation = null;
//
//        for (Location location : locationResult.getLocations()) {
//            if (location != null) {
//                if (bestLocation == null) {
//                    bestLocation = location;
//                } else {
//                    // Compare accuracy
//                    if (location.getAccuracy() < bestLocation.getAccuracy()) {
//                        bestLocation = location;
//                    }
//                    // If the accuracy is similar, prefer the most recent one
//                    else if (location.getAccuracy() == bestLocation.getAccuracy() &&
//                            location.getTime() > bestLocation.getTime()) {
//                        bestLocation = location;
//                    }
//                }
//            }
//        }
//
//        return bestLocation;
//    }
//
//    public static String getDeviceName() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        }
//        return capitalize(manufacturer) + " " + model;
//    }
//
//    public static String getStackTraceAsString() {
//        StringBuilder sb = new StringBuilder();
//        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
//            sb.append(element.toString()).append("\n");
//        }
//        return sb.toString();
//    }
//}