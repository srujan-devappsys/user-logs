package com.example.myapplication.internal;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;

public class StsManager {
    private LogsPlugin config;
    public  static String getPresignedUrl(LogsPlugin config){
            try{

                String urlString = "http://192.168.1.10:8080/track/user_logs"
                        + "?client=" + config.getClient()
                        + "&project=" + config.getProject()
                        + "&userId=" + config.getUserId();

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    response.append(line);
                }
                JSONObject json =new JSONObject(response.toString());
                Log.d("This is the url",json.getString("url"));
                String path = json.getString("path");

                Log.d("UPLOAD", "Uploading to: " + path);
                return response.toString();
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


}
