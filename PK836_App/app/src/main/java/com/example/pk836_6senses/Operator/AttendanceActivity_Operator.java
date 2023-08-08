package com.example.pk836_6senses.Operator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AttendanceActivity_Operator extends MyBaseActivity_Operator {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    User user;
    Button btn_absent, btn_present;
    String str_attendance, uid, date_n_time, str_lat, str_long;
    ProgressBar progressBar;
    locationSave_Operator locationSave_operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_attendance_operator, null, false);
        drawer.addView(v, 0);

        btn_absent = findViewById(R.id.btn_absent);
        btn_present = findViewById(R.id.btn_present);
        progressBar = findViewById(R.id.progressbar);


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();

        user = SharedPrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getUSERID());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        date_n_time = sdf.format(new Date());

        locationSave_operator = SharedPrefManager.getInstance(this).getSavedLocation();
        str_lat = String.valueOf(locationSave_operator.getOP_LATS());
        str_long = String.valueOf(locationSave_operator.getOP_LONGS());

        btn_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_attendance = "ABSENT";
                makeattendance();
            }
        });

        btn_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_attendance = "PRESENT";
                makeattendance();

            }
        });
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

    private void makeattendance() {

        @SuppressLint("StaticFieldLeak")
        class UserLogin extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent Page = new Intent(AttendanceActivity_Operator.this, Home_Activity_Operator.class);
                        startActivity(Page);

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("OP_ID", uid);
                params.put("OP_ATTENDENCE", str_attendance);
                params.put("ATTEND_DATE", date_n_time);
                params.put("OP_LAT", str_lat);
                params.put("OP_LONG", str_long);

                Log.e("OP_ID", uid);
                Log.e("OP_ATTENDENCE", str_attendance);
                Log.e("ATTEND_DATE", date_n_time);
                Log.e("OP_LAT", str_lat);
                Log.e("OP_LONG", str_long);


                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_ATTENDANCE, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();

    }
}