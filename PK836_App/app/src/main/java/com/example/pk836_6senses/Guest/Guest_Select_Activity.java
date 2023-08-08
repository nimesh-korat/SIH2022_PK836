package com.example.pk836_6senses.Guest;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pk836_6senses.Citizen.Add_Appointment;
import com.example.pk836_6senses.Guest.Guest_Select_Activity;
import com.example.pk836_6senses.Guest.Guest_Select_Activity;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Guest_Select_Activity extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    Marker marker;
    LatLng latLng;
    String Lati, Longi, mark_lat, mark_long;
    Button cnfm_location;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        setContentView(R.layout.activity_guest_select);

        cnfm_location = findViewById(R.id.btn_cnfm_lctnActivity_guest);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_select_loc);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Guest_Select_Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurruntLocation();
        } else {
            ActivityCompat.requestPermissions(Guest_Select_Activity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();

    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurruntLocation();
            }
        }
    }

    private void getCurruntLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                Lati = String.valueOf(location.getLatitude());
                Longi = String.valueOf(location.getLongitude());

                if (Lati != null && Longi != null) {
                    cnfm_location.setVisibility(View.VISIBLE);
                } else {
                    cnfm_location.setVisibility(View.GONE);
                }

                cnfm_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.e("mark_lat", mark_lat);
                        Log.e("mark_long", mark_long);
                        Intent intent = new Intent(Guest_Select_Activity.this, Add_Appointment.class);
                        intent.putExtra("Lat", mark_lat);
                        intent.putExtra("Long", mark_long);
                        startActivity(intent);


                    }
                });

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        mMap.setOnMapLongClickListener(Guest_Select_Activity.this);
                        mMap.setOnMarkerDragListener(Guest_Select_Activity.this);

                        MarkerOptions options = new MarkerOptions();
                        options.position(latLng);
                        options.icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_marker_select_location));
                        marker = mMap.addMarker(options);
                        marker.setDraggable(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        mMap.getUiSettings().setMapToolbarEnabled(false);
                        mMap.setMyLocationEnabled(true);
                        mark_lat = String.valueOf(latLng.latitude);
                        mark_long = String.valueOf(latLng.longitude);

                    }
                });
            }
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        //mMap.clear();
        if (marker != null) {
            marker.remove();
            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(String.valueOf(latLng))
                    .draggable(true)
            );
            mark_lat = String.valueOf(latLng.latitude);
            mark_long = String.valueOf(latLng.longitude);
            marker.showInfoWindow();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
        latLng = marker.getPosition();
        Log.e("LAT START", String.valueOf(latLng.latitude));
        Log.e("LONG START", String.valueOf(latLng.longitude));
        marker.setTitle(null);
        // marker.showInfoWindow();
        mark_lat = String.valueOf(latLng.latitude);
        mark_long = String.valueOf(latLng.longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
        latLng = marker.getPosition();
        Log.e("LAT DRAG", String.valueOf(latLng.latitude));
        Log.e("LONG DRAG", String.valueOf(latLng.longitude));
        marker.setTitle(String.valueOf(latLng));
        //marker.showInfoWindow();
        mark_lat = String.valueOf(latLng.latitude);
        mark_long = String.valueOf(latLng.longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        latLng = marker.getPosition();
        Log.e("LAT END", String.valueOf(latLng.latitude));
        Log.e("LONG END", String.valueOf(latLng.longitude));
        mark_lat = String.valueOf(latLng.latitude);
        mark_long = String.valueOf(latLng.longitude);

        marker.setTitle(String.valueOf(latLng));
        //   marker.showInfoWindow();


        /*
        Log.e("latlng", String.valueOf(latLng));*/
       /* try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

}