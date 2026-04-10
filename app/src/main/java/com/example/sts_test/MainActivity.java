package com.example.sts_test;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.internal.LoggingSDK;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Map<String, Object> props = new HashMap<>();
        props.put("screen", "MainActivity");
        new Handler().postDelayed(() -> {
            LoggingSDK.log(1,"App Started",props);
        }, 2000);
       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
       getLocations();
    }

    private void getLocations() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED)
        {
        ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        100);
        return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if(location !=null) {
                double lat =location.getLatitude();
                double lon =location.getLongitude();
                Log.d("Latitude","Latitude is" +lat);
                Log.d("Longitude","Longitude is" +lon);
                LoggingSDK.updateLocation(lat,lon);
            }
            else {

                Log.d("LOCATION", "Location is "+location);
            }
        });
    }

}

