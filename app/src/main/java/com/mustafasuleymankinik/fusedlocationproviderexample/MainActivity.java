package com.mustafasuleymankinik.fusedlocationproviderexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    TextView textView1, textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildLocationRequest();
        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setSmallestDisplacement(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildLocationCallBack();


    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    textView1.setText("Latitude: " + location.getLatitude());
                    textView2.setText("Longitude: " + location.getLongitude());

                }
            }
        };
    }

    public void requestUpdates(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        } else {
            fusedLocationClient.requestLocationUpdates
                    (
                            locationRequest,
                            locationCallback,
                            null
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, "Process is success", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void stopUpdates(View view) {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void lastLocation(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        else
        {
            Task task = fusedLocationClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    textView1.setText("Latitude: "+location.getLatitude());
                    textView2.setText("Longitude "+location.getLongitude());

                }
            });
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2&&permissions.length>1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationClient.requestLocationUpdates
                    (
                            locationRequest,
                            locationCallback,
                            null
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, "Process is success", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(requestCode==1&&permissions.length>1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Task task = fusedLocationClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    textView1.setText("Latitude: "+location.getLatitude());
                    textView2.setText("Longitude "+location.getLongitude());

                }
            });
        }
    }


}