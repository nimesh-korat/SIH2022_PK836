package com.example.pk836_6senses.Citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.pk836_6senses.Model.Appt_Model;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.example.pk836_6senses.directionhelpers.FetchURL;
import com.example.pk836_6senses.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.royrodriguez.transitionbutton.TransitionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Active_apnt_Citizen extends MyBaseActivity_Citizen implements OnMapReadyCallback, TaskLoadedCallback {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    private GoogleMap map;
    User user;
    String uid, S_OP_F_NAME, S_OP_L_NAME, S_OP_PHONE_NO, S_OP_LAT, S_OP_LONG, S_U_LATITUDE, S_U_LONGITUDE, S_BEHAVIOUR, S_ACCURACY;
    Button op_call;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    MapFragment mapFragment;
    /*PinView show_otp;*/
    TextView op_name, op_behavior, op_accuracy;
    LatLng latLng;
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_active_apnt_citizen, null, false);
        drawer.addView(v, 0);


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
        user = SharedPrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getUSERID());

        op_call = findViewById(R.id.btn_op_call);
        //show_otp = findViewById(R.id.show_otp);
        op_name = findViewById(R.id.get_op_name);
        op_behavior = findViewById(R.id.get_op_behaviour);
        op_accuracy = findViewById(R.id.get_op_accuracy);

        //show_otp.setLongClickable(false);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_get_direction);


        op_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + S_OP_PHONE_NO));
                startActivity(callIntent);
            }
        });
        getOpLocation();
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

        map.addMarker(place1);
        map.addMarker(place2);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
       map.getUiSettings().setMapToolbarEnabled(false);

    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getOpLocation();
                doTheAutoRefresh();
            }
        }, 60000);
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
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    private void getOpLocation() {

        class GetOpLocation extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // progressBar = (ProgressBar) findViewById(R.id.progressBar_login_citizen);
                // progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = obj.getJSONArray("Appointments");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            S_OP_F_NAME = jsonObject1.getString("OP_F_NAME");
                            S_OP_L_NAME = jsonObject1.getString("OP_L_NAME");
                            S_OP_PHONE_NO = jsonObject1.getString("OP_PHONE_NO");
                            S_OP_LAT = jsonObject1.getString("OP_LAT");
                            S_OP_LONG = jsonObject1.getString("OP_LONG");
                            S_BEHAVIOUR = jsonObject1.getString("BEHAVIOUR");
                            S_ACCURACY = jsonObject1.getString("ACCURACY");
                            S_U_LATITUDE = jsonObject1.getString("U_LATITUDE");
                            S_U_LONGITUDE = jsonObject1.getString("U_LONGITUDE");

                        }

                        op_name.setText(S_OP_F_NAME + " " + S_OP_L_NAME);
                        op_behavior.setText("BEHAVIOUR: " + S_BEHAVIOUR);
                        op_accuracy.setText("ACCURACY: " + S_ACCURACY);
                        mapFragment.getMapAsync(Active_apnt_Citizen.this);

                        place1 = new MarkerOptions().position(new LatLng(Double.valueOf(S_OP_LAT), Double.valueOf(S_OP_LONG))).title("OPERATOR");
                        place2 = new MarkerOptions().position(new LatLng(Double.valueOf(S_U_LATITUDE), Double.valueOf(S_U_LONGITUDE))).title("HOME");
                        int i = 0;
                        i = i + 1;
                        Log.e("i", String.valueOf(i));
                        String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                        new FetchURL(Active_apnt_Citizen.this).execute(url, "driving");
                        latLng = new LatLng(Double.valueOf(S_OP_LAT), Double.valueOf(S_OP_LONG));

                        doTheAutoRefresh();
                    } else {

                       // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                Log.e("aaa", uid);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_USER_GET_ACPT_APPT_DATA, params);
            }
        }

        GetOpLocation ul = new GetOpLocation();
        ul.execute();
    }
}