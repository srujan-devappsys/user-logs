package com.example.sts_test;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.myapplication.internal.LoggingSDK;
import com.example.myapplication.internal.LogsPlugin;

import java.util.List;
import java.util.Locale;

public class MyApplication extends Application{
    @Override
        public void onCreate() {
            super.onCreate();
        LogsPlugin config = new LogsPlugin.Builder()
                .setClient("Client1")
                .setProject("project1")
                .setUserId("user2")
                .setHost("server.com")
                .setPort(9090)
                .setBatchSize(3)
                .setCarrier(getCarrier(this))
                .build();
        LoggingSDK.init(this, config);

        }
    public static String getCarrier(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : "Unknown";
    }
    public static String getCity(Context context, double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            return addresses.get(0).getLocality();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getRegion(Context context, double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            return addresses.get(0).getAdminArea();
        } catch (Exception e) {
            return "";
        }
    }
    public static double getLatitude(Location location) {
        return location != null ? location.getLatitude() : 0.0;
    }

    public static double getLongitude(Location location) {
        return location != null ? location.getLongitude() : 0.0;
    }
}
