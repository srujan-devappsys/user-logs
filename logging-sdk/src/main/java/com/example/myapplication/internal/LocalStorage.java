package com.example.myapplication.internal;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LocalStorage {
        public static final  String FILE_NAME ="logs.txt";
    public static void savelog(Context context, String message) {
        try {
         JSONObject logJson = new JSONObject(message);
//            SimpleDateFormat sdf = new SimpleDateFormat(
//                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
//                    Locale.getDefault()
//            );
//            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            String time = sdf.format(new Date());
//            logJson.put("message", message);

            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write((logJson + "\n").getBytes());
            fos.close();
            Log.d("LOCAL_STORAGE", "Saved JSON log: " + logJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static String readLogs(Context context){
            try{
                FileInputStream fis =context.openFileInput(FILE_NAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder logs =new StringBuilder();
                String line;
                while ((line = reader.readLine()) !=null )
                {
                    logs.append(line).append("\n");
                }
                return logs.toString();
            }catch (Exception e) {
                return "";
            }
        }
        public static void clearLogs(Context context) {
            try {
                FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                fos.write("".getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}
