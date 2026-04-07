package com.example.sts_test;
import android.app.Application;
import com.example.myapplication.internal.LoggingSDK;
import com.example.myapplication.internal.LogsPlugin;
public class MyApplication extends Application{
    @Override
        public void onCreate() {
            super.onCreate();
        LogsPlugin config = new LogsPlugin.Builder()
                .setClient("Client1")
                .setProject("project1")
                .setUserId("user1")
                .build();
            LoggingSDK.init(this,config);
        }
}
