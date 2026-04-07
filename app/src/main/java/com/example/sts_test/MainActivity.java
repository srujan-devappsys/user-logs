package com.example.sts_test;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.internal.LoggingSDK;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        LoggingSDK.log("App Started");
        LoggingSDK.log("App Started");
        LoggingSDK.log("App Starteddddd");
        LoggingSDK.log("App Starteddddd");
    }
}

