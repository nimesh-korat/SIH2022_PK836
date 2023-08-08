package com.example.pk836_6senses.Operator;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home_Activity_Operator extends MyBaseActivity_Operator {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    FusedLocationProviderClient fusedLocationProviderClient;
    String lat, longi, op_id, status_date_time, city, strPend, strComp;
    User user;
    LocationRequest locationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleMap mMap;
    Marker marker;
    LatLng latLng;
    TextView completedtv, pendingtv, txt_behave, txt_accuracy;
    SupportMapFragment mapView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    protected static final int LOCATION_PERMISSION = 44;
    CardView review_card, activeAppt;


    private static final int MY_FINE_LOCATION_REQUEST = 99;
    private static final int MY_BACKGROUND_LOCATION_REQUEST = 100;

    BGLocationService_Operator mLocationService = new BGLocationService_Operator();
    Intent mServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_home_operator, null, false);
        drawer.addView(v, 0);
        //mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.homeMap);

        completedtv = findViewById(R.id.tv_completedtv);
        pendingtv = findViewById(R.id.tv_pendingtv);

        mSwipeRefreshLayout = findViewById(R.id.swipeTorefreshOP);
        activeAppt = findViewById(R.id.op_active_appt);
        review_card = findViewById(R.id.review_card);


        findViewById(R.id.completedtv).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
        findViewById(R.id.pendingtv).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translatelight));
        findViewById(R.id.review_card).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
        findViewById(R.id.op_active_appt).startAnimation(AnimationUtils.loadAnimation(this,R.anim.translatelight));

        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               /* getLocation();
                getCurruntLocation();*/
                getTotalData();
                asklocation();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        user = SharedPrefManager.getInstance(this).getUser();
        op_id = String.valueOf(user.getUSERID());
        Log.e("op", op_id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        status_date_time = sdf.format(new Date());

        asklocation();
        getTotalData();

        review_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity_Operator.this, ShowReviews.class);
                startActivity(intent);
            }
        });

        activeAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Activity_Operator.this, Active_apnt_Operator.class);
                startActivity(intent);
            }
        });

    }

    //ask location permission
    private void asklocation() {

        if (ActivityCompat.checkSelfPermission(Home_Activity_Operator.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            settingsrequest();

            //getLocation();
            //getCurruntLocation();
        } else {
            ActivityCompat.requestPermissions(Home_Activity_Operator.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                    getCurruntLocation();
                    getupdatingLocation();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(Home_Activity_Operator.this, REQUEST_CHECK_SETTINGS);
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

    private void getupdatingLocation() {
        if (ActivityCompat.checkSelfPermission(Home_Activity_Operator.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ActivityCompat.checkSelfPermission(Home_Activity_Operator.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {


                    AlertDialog alertDialog = new AlertDialog.Builder(Home_Activity_Operator.this).create();
                    alertDialog.setTitle("Background permission");
                    alertDialog.setMessage(getString(R.string.service_grant_permission));

              /*      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start service anyway",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    starServiceFunc();
                                    dialog.dismiss();
                                }
                            });*/

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Grant background Permission",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    requestBackgroundLocationPermission();
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();


                } else if (ActivityCompat.checkSelfPermission(Home_Activity_Operator.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    starServiceFunc();
                }
            } else {
                starServiceFunc();
            }

        } else if (ActivityCompat.checkSelfPermission(Home_Activity_Operator.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home_Activity_Operator.this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                AlertDialog alertDialog = new AlertDialog.Builder(Home_Activity_Operator.this).create();
                alertDialog.setTitle("ACCESS_FINE_LOCATION");
                alertDialog.setMessage("Location permission required");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                requestFineLocationPermission();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            } else {
                requestFineLocationPermission();
            }
        }
    }

    private void starServiceFunc() {
        mLocationService = new BGLocationService_Operator();
        mServiceIntent = new Intent(this, mLocationService.getClass());
        if (!UtilForLocation_Operator.isMyServiceRunning(mLocationService.getClass(), this)) {
            startService(mServiceIntent);
           // Toast.makeText(this, "Service started successfully", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(this, "Service is already running", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                MY_BACKGROUND_LOCATION_REQUEST);
    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, MY_FINE_LOCATION_REQUEST);
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
                        getCurruntLocation();
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

        if (requestCode == MY_FINE_LOCATION_REQUEST) {

            if (grantResults.length != 0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    requestBackgroundLocationPermission();
                }

            } else {
               // Toast.makeText(this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_LONG).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                 /*   startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.getPackageName(), null),),);*/

                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package: com.example.pk836_6senses")
                    ));

                }
            }

        } else if (requestCode == MY_BACKGROUND_LOCATION_REQUEST) {

            if (grantResults.length != 0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Background location Permission Granted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG).show();
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
                        Geocoder geocoder = new Geocoder(Home_Activity_Operator.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        mSwipeRefreshLayout.setRefreshing(false);

                        lat = String.valueOf(addresses.get(0).getLatitude());
                        longi = String.valueOf(addresses.get(0).getLongitude());
                        city = String.valueOf(addresses.get(0).getLocality());

                        // addLocationToServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    //show location pin on mapview
    private void getCurruntLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

                } else {
                    //Toast.makeText(Home_Activity_Operator.this, "Null Location", Toast.LENGTH_SHORT).show();
                    getCurruntLocation();
                }
            }
        });
    }
    // add or update lat & long on server
/*
    private void addLocationToServer() {
        class AddLocationToServer extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject objs = new JSONObject(s);

                    //if no error in response
                    if (!objs.getBoolean("error")) {
                        locationSave_Operator locationSave_operator = new locationSave_Operator(
                                lat,
                                longi
                        );

                        //save in shared pref..
                        SharedPrefManager.getInstance(getApplicationContext()).locationPref(locationSave_operator);
                        //Toast.makeText(getApplicationContext(), objs.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), String.valueOf(objs.getBoolean("error")), Toast.LENGTH_SHORT).show();
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

                params.put("OP_ACT_STATUS", "LOGGED_IN");
                params.put("OP_LAT", lat);
                params.put("OP_LONG", longi);
                params.put("OP_ID", op_id);
                params.put("STATUS_DATE_TIME", status_date_time);


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_ACTIVE_STATUS, params);
            }
        }
        AddLocationToServer ul = new AddLocationToServer();
        ul.execute();
    }
*/

    //get total appt data count
    private void getTotalData() {

        class getTotalData extends AsyncTask<Void, Void, String> {

            //final ProgressBar progressBar = new ProgressBar(Home_Activity_Operator.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                // progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object

                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("TOTAL_OP_COUNT");

                        //creating a new user object
                        strComp = userJson.getString("COMPLETED_COUNT");
                        strPend = userJson.getString("PENDING_COUNT");
                        if (strComp.equals("null")){
                            strComp="0";
                        }
                        if (strPend.equals("null")){
                            strPend="0";
                        }


                        ValueAnimator completed = ValueAnimator.ofInt(0, Integer.parseInt(strComp));
                        completed.setDuration(1000);
                        completed.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                completedtv.setText(completed.getAnimatedValue().toString());
                                //this value should be used to update properties of views.
                                //just don't forget to run invalidate function of your views
                                // to redraw them.
                            }
                        });
                        completed.start();
                        mSwipeRefreshLayout.setRefreshing(false);
                        ValueAnimator pending = ValueAnimator.ofInt(0, Integer.parseInt(strPend));
                        pending.setDuration(1000);
                        pending.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                pendingtv.setText(pending.getAnimatedValue().toString());
                                //this value should be used to update properties of views.
                                //just don't forget to run invalidate function of your views
                                // to redraw them.
                            }
                        });
                        pending.start();

                    } else {

                        //Toast.makeText(getApplicationContext(), String.valueOf(obj.getBoolean("error")), Toast.LENGTH_SHORT).show();
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
                params.put("OP_ID", op_id);


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_GET_COUNT, params);
            }
        }

        getTotalData ul = new getTotalData();
        ul.execute();
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
}