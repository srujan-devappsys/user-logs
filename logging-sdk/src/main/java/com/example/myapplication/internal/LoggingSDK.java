package com.example.myapplication.internal;

import android.content.Context;

public class LoggingSDK {
    private static Plugin plugin;

    public static LoggingSDK instance;

    public static void init(Context context,LogsPlugin config) {
            if (plugin == null) {
                plugin = Plugin.init(context,config);
            }
    }
    public static void log(String message)
    {
        if (plugin == null) {
            throw new IllegalStateException("SDK not initialized");
        }
        plugin.log(message);
    }
    }
