package com.example.pk836_6senses.Citizen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;

public class Comp_Appt_Details_Citizen extends MyBaseActivity_Citizen {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    TextView id, booked_date, cmpt_date, op_name, appt_detail, address, pincode, city, state;
    CardView add_review;
    RateUsDialog_citizen rateUsDialog;
    public static String appt_id, op_IDS;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_comp_appt_details_citizen, null, false);
        drawer.addView(v, 0);


        id = findViewById(R.id.appt_id);
        op_name = findViewById(R.id.history_op_name);
        booked_date = findViewById(R.id.booked_date);
        cmpt_date = findViewById(R.id.history_appt_cmpt_date);
        appt_detail = findViewById(R.id.history_update_details);
        address = findViewById(R.id.history_address);
        pincode = findViewById(R.id.history_pincode);
        city = findViewById(R.id.history_city);
        state = findViewById(R.id.history_state);
        add_review = findViewById(R.id.add_review);


        Intent intent = getIntent();
        id.setText("Appointment Id: " + intent.getStringExtra("ID"));
        op_name.setText("Name: " + intent.getStringExtra("NAME"));
        booked_date.setText("Booked Date: " + intent.getStringExtra("BOOKED DATE"));
        cmpt_date.setText("Completed Date: " + intent.getStringExtra("COMPLETED DATE"));
        appt_detail.setText("Update Details: " + intent.getStringExtra("APPT_DETAIL"));
        address.setText("Address: " + intent.getStringExtra("ADDRESS"));
        pincode.setText("Pincode: " + intent.getStringExtra("PINCODE"));
        city.setText("City: " + intent.getStringExtra("CITY"));
        state.setText("State: " + intent.getStringExtra("STATE"));

        appt_id = intent.getStringExtra("ID");
        op_IDS = intent.getStringExtra("OP_ID");

        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateUsDialog = new RateUsDialog_citizen(Comp_Appt_Details_Citizen.this);
                rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent)));
                rateUsDialog.setCancelable(true);
                rateUsDialog.show();
            }
        });


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
}