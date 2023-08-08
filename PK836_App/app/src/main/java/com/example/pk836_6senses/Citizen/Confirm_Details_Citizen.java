package com.example.pk836_6senses.Citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
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

public class Confirm_Details_Citizen extends MyBaseActivity_Citizen {


    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    TextView cnf_name, cnf_phone, cnf_address, cnf_pin, cnf_city, cnf_state, cnf_detail;
    CheckBox cnf_checkbox;
    Button back, confirm;
    String str_cnf_name, str_cnf_phone, str_cnf_address, str_cnf_pin, str_cnf_city, str_cnf_state, str_cnf_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_confirm_details_citizen, null, false);
        drawer.addView(v, 0);

        cnf_name = findViewById(R.id.cnfm_name);
        cnf_phone = findViewById(R.id.cnfm_phone);
        cnf_address = findViewById(R.id.cnfm_address);
        cnf_pin = findViewById(R.id.cnfm_pincode);
        cnf_city = findViewById(R.id.cnfm_city);
        cnf_state = findViewById(R.id.cnfm_state);
        cnf_detail = findViewById(R.id.cnfm_detail);
        cnf_checkbox = findViewById(R.id.cnfm_checkbox);
        back = findViewById(R.id.btn_back_cnfm);
        confirm = findViewById(R.id.btn_confirm);

        str_cnf_name = cnf_name.getText().toString();
        str_cnf_phone = cnf_phone.getText().toString();
        str_cnf_address = cnf_address.getText().toString();
        str_cnf_pin = cnf_pin.getText().toString();
        str_cnf_city = cnf_city.getText().toString();
        str_cnf_state = cnf_state.getText().toString();
        str_cnf_detail = cnf_detail.getText().toString();




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
}