package com.example.myapplication.internal;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.provider.Settings;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilityy {
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static String getCountry() {
        return Locale.getDefault().getDisplayCountry();
    }
    public static String getDeviceType() {
        return "Android";
    }
    public static String getDeviceFamily() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }
    public static String getOsName() {
        return "Android";
    }
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }
    public static String getCity(Context context, double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getLocality(); // City
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getRegion(Context context, double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAdminArea(); // State
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getEventTime() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(new Date());
    }
}

