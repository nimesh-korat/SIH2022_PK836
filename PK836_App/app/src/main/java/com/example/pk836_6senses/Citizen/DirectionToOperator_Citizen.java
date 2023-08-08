package com.example.pk836_6senses.Citizen;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.directionhelpers.FetchURL;
import com.example.pk836_6senses.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class DirectionToOperator_Citizen extends MyBaseActivity_Citizen implements OnMapReadyCallback, TaskLoadedCallback {


    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    private GoogleMap map;
    Button btnGetDirection;
    private MarkerOptions place1,place2;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_direction_to_operator_citizen, null, false);
        drawer.addView(v, 0);

        btnGetDirection = findViewById(R.id.btn_get_direction);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_get_direction);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(23.0186603,72.6080682)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(23.038379,72.652803)).title("Location 2");

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                new FetchURL(DirectionToOperator_Citizen.this).execute(url, "driving");
               // new FetchURL(DirectionToCitizen_Operator.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
    }

    //For No Internet
    public void InternetStatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    @Override
    public void onResume(){
        super.onResume();
        try{
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }catch (Exception e){
            // already registered
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        Log.d("mylog", "Added Markers");

        map.addMarker(place1);
        map.addMarker(place2);

    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}