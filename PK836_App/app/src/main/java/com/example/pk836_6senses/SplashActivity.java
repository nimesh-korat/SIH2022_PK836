package com.example.pk836_6senses;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pk836_6senses.Citizen.Home_Activity_Citizen;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.Operator.Home_Activity_Operator;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashActivity extends AppCompatActivity {
    User user;
    String role;
    LocationRequest locationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int LOCATION_PERMISSION = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        setContentView(R.layout.activity_splash);

        user = SharedPrefManager.getInstance(this).getUser();
        role = String.valueOf(user.getROLE());

        asklocation();


    }

    private void asklocation() {

        if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            settingsrequest();

        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);

        }

    }

    public void settingsrequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true); //this is the key ingredient

        Task<LocationSettingsResponse> locationSettingsResponseTask =
                LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    //Toast.makeText(SplashActivity.this, "GPS is already Turned On", Toast.LENGTH_SHORT).show();
                    starthomeActivity();
                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        //Toast.makeText(SplashActivity.this, "Settings Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //Toast.makeText(this, "Now GPS is Enabled", Toast.LENGTH_SHORT).show();
                        starthomeActivity();
                        break;
                    case Activity.RESULT_CANCELED:
                        settingsrequest();
                        //Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Now Permission is Enabled", Toast.LENGTH_SHORT).show();
                    settingsrequest();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();//keep asking if imp or do whatever
                    asklocation();
                }
        }
    }

    private void starthomeActivity() {
        //Delayer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    finish();
                    if (role.equals("OPERATOR")) {
                        Intent intent = new Intent(SplashActivity.this, Home_Activity_Operator.class);
                        startActivity(intent);
                    } else if (role.equals("CITIZEN")) {
                        Intent intent = new Intent(SplashActivity.this, Home_Activity_Citizen.class);
                        startActivity(intent);
                    }


                } else {
                    Intent i = new Intent(SplashActivity.this, SelectUser.class);
                    startActivity(i);
                    // close this activity
                    finish();

                }
            }
        }, 2 * 1000); // wait for 2 seconds
    }
}