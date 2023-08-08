package com.example.pk836_6senses.Citizen;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.No_Internet.NoOpFound;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home_Activity_Citizen extends MyBaseActivity_Citizen {


    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    PDFView pdfView;
    CardView book_aapt, view_appt, card_instruction, book_appt_OnPortal;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    protected static final int LOCATION_PERMISSION = 44;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String lat, longi, city, u_city, pin, uid, phone;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_home_citizen, null, false);
        drawer.addView(v, 0);

        findViewById(R.id.card_book_appt).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
        findViewById(R.id.card_view_appt).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translatelight));
        findViewById(R.id.card_instruction).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
        findViewById(R.id.book_onPortal).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translatelight));

//        pdfView = findViewById(R.id.pdfView);
        book_aapt = findViewById(R.id.card_book_appt);
        view_appt = findViewById(R.id.card_view_appt);
        card_instruction = findViewById(R.id.card_instruction);
        book_appt_OnPortal = findViewById(R.id.book_onPortal);

        card_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity_Citizen.this, Pdf_Citizen.class);
                startActivity(intent);
            }
        });


//        pdfView.fromAsset("abc.pdf").load();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        book_aapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ckeckNearbyOp();

                /*Intent intent = new Intent(Home_Activity_Citizen.this, Add_Appointment.class);
                startActivity(intent);*/
            }
        });
        book_appt_OnPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity_Citizen.this, Book_appt_OnPortal.class);
                startActivity(intent);
            }
        });

        view_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity_Citizen.this, Active_apnt_Citizen.class);
                startActivity(intent);
            }
        });



        user = SharedPrefManager.getInstance(this).getUser();
        u_city = String.valueOf(user.getCITY());
        uid = String.valueOf(user.getUSERID());
        phone = String.valueOf(user.getPHONE_NO());

        asklocation();


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();


    }


    //For No Internet
    public void InternetStatus() {
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e) {
            // already registered
        }
    }

    //ask location permission
    private void asklocation() {

        if (ActivityCompat.checkSelfPermission(Home_Activity_Citizen.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            settingsrequest();

            //getLocation();
            //getCurruntLocation();
        } else {
            ActivityCompat.requestPermissions(Home_Activity_Citizen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        }

    }

    //ask for GPS TurnOn
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

                    getLocation();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(Home_Activity_Citizen.this, REQUEST_CHECK_SETTINGS);
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
                        getLocation();
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

    //get lat long and city
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(Home_Activity_Citizen.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        lat = String.valueOf(addresses.get(0).getLatitude());
                        longi = String.valueOf(addresses.get(0).getLongitude());
                        city = String.valueOf(addresses.get(0).getLocality());
                        pin = String.valueOf(addresses.get(0).getPostalCode());

                        // addLocationToServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(Home_Activity_Citizen.this, "Can not get your location, Please Verify Location Permissions", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void ckeckNearbyOp() {

        //if everything is fine

        class ckeckNearbyOp extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


                        Intent Page = new Intent(Home_Activity_Citizen.this, SelectLocationActivity.class);
                        startActivity(Page);

                    } else {

                        addrequestforop();

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        /*Intent Page1 = new Intent(Home_Activity_Citizen.this, NoOpFound.class);
                        startActivity(Page1);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("CITY", u_city);
                params.put("LAT", lat);
                params.put("LONG", longi);
                Log.e("city", u_city);
                Log.e("LAT", lat);
                Log.e("LONG", longi);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_CHECK_NEARBY_OP, params);
            }

        }
        ckeckNearbyOp ul = new ckeckNearbyOp();
        ul.execute();
    }

    private void addrequestforop() {

        class ckeckNearbyOp extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        /*Intent Page1 = new Intent(Home_Activity_Citizen.this, NoOpFound.class);
                        startActivity(Page1);*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("USERID", uid);
                params.put("U_PINCODE", pin);
                params.put("U_PHONE_NO", phone);
                Log.e("city", u_city);
                Log.e("LAT", lat);
                Log.e("LONG", longi);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_ADD_OP_REQUEST, params);
            }

        }
        ckeckNearbyOp ul = new ckeckNearbyOp();
        ul.execute();

    }

}