package com.example.pk836_6senses.Citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Pending_Apnt_Detail_Citizen extends MyBaseActivity_Citizen {

    TextView appt_id,name, phone, address,booked_date, pin,  city, state, appt_detail;
    Button btn_cancle;
    String uid, str_appt_id;
    User user;

    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_pending_apnt_detail_citizen, null, false);
        drawer.addView(v, 0);


        appt_id = findViewById(R.id.appt_id);
        name = findViewById(R.id.appt_detail_name);
        booked_date = findViewById(R.id.appt_booked_date);
        phone = findViewById(R.id.appt_detail_phone);
        appt_detail = findViewById(R.id.appt_detail_detail);
        address = findViewById(R.id.appt_detail_address);
        pin = findViewById(R.id.appt_detail_pincode);
        city = findViewById(R.id.appt_detail_city);
        state = findViewById(R.id.appt_detail_state);
        btn_cancle = findViewById(R.id.btn_citizen_cancel_detail);

        user = SharedPrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getUSERID());

        Intent intent = getIntent();
        str_appt_id = intent.getStringExtra("APPT_ID");
        appt_id.setText("Name: "+intent.getStringExtra("APPT_ID"));
        booked_date.setText("Name: "+intent.getStringExtra("BOOKED_DATE"));
        name.setText("Name: "+intent.getStringExtra("NAME"));
        phone.setText("Phone: "+intent.getStringExtra("PHONE"));
        appt_detail.setText("Details: "+intent.getStringExtra("APPT_DETAIL"));
        address.setText("Address: "+intent.getStringExtra("ADDRESS"));
        pin.setText("Pin: "+intent.getStringExtra("PINCODE"));
        city.setText("City: "+intent.getStringExtra("CITY"));
        state.setText("State: "+intent.getStringExtra("STATE"));

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCancelAppt();
            }
        });

        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
    }

    private void userCancelAppt() {

        class UserLogin extends AsyncTask<Void, Void, String> {


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

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent Page = new Intent(Pending_Apnt_Detail_Citizen.this, Home_Activity_Citizen.class);
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

                //returing the response
                params.put("APPT_ID", str_appt_id);
                params.put("USERID", uid);

                return requestHandler.sendPostRequest(URLs.URL_USER_CANCEL_APPT, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();

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
}