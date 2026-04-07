package com.example.myapplication.internal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class S3Uploader {
        public  static boolean uploadToS3(String presignedUrl,String JsonData)
        {
            try {
                URL url =new URL(presignedUrl);

                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("PUT");

                OutputStream os =conn.getOutputStream();
                os.write(JsonData.getBytes());
                os.close();

                int responsecode =conn.getResponseCode();
                return responsecode >= 200 && responsecode < 300;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

}
