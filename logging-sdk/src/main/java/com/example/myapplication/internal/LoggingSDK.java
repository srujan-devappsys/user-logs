package com.example.myapplication.internal;

import android.content.Context;
import android.util.Log;

import java.util.Locale;
import java.util.Map;

public class LoggingSDK {
    private static Plugin plugin;
    public static LoggingSDK instance;
    private static double latitude;
    private static double longitude;

    public static void init(Context context,LogsPlugin config) {
        Log.d("This is good", Locale.getDefault().getCountry());
            if (plugin == null) {
                plugin = Plugin.init(context,config);
            }
    }
    public static void log(int level, String message, Map<String, Object> properties)
    {
        if (plugin == null) {
            throw new IllegalStateException("SDK not initialized");
        }
        plugin.log(level, message, properties);
    }


    public static void updateLocation(double lat, double lon) {
        latitude = lat;
        longitude = lon;
        plugin.updateLocation(lat,lon);
    }
    }
