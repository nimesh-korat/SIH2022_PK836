package com.example.pk836_6senses.Operator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Active_apnt_Operator extends MyBaseActivity_Operator {

    TextView citizen_name,appt_id,booked_dt,accepted_dt,update_details,address,city,state,pincode;
    String uid,apid, useridappt,U_LONGITUDE,U_LATITUDE, S_CITIZEN_F_NAME, S_CITIZEN_L_NAME, S_CITIZEN_PHONE_NO, S_APPT_ID, S_APNT_PEND_DT, S_APNT_ACPT_DT, S_APNT_DETAIL, S_U_ADDRESS, S_U_UPDATE_DETAILS, S_U_CITY, S_U_STATE, S_U_PINCODE;
    User user;
    Button call_citizen,submit_otp,get_direction, cancel_appt, btn_want_to_cancel_appt;
    PinView enter_otp;
    String opName;
    TextInputLayout addreason;

    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_active_apnt_operator, null, false);
        drawer.addView(v, 0);

        appt_id = findViewById(R.id.history_appt_id);
        citizen_name = findViewById(R.id.history_get_citizen_name);
        booked_dt = findViewById(R.id.history_booked_date);
        accepted_dt = findViewById(R.id.history_accepted_date);
        update_details = findViewById(R.id.history_op_update_details);
        address = findViewById(R.id.history_op_address);
        city = findViewById(R.id.history_op_city);
        state = findViewById(R.id.history_op_stae);
        pincode = findViewById(R.id.history_op_pincode);
        btn_want_to_cancel_appt = findViewById(R.id.btn_op_want_cancel_appt);
        cancel_appt = findViewById(R.id.btn_op_cancel_appt);
        addreason = findViewById(R.id.op_reason_for_cancle);

        btn_want_to_cancel_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addreason.setVisibility(View.VISIBLE);
                cancel_appt.setVisibility(View.VISIBLE);
                btn_want_to_cancel_appt.setVisibility(View.GONE);
            }
        });

        call_citizen = findViewById(R.id.btn_call_citizen);
        submit_otp = findViewById(R.id.op_submit_otp);
        get_direction = findViewById(R.id.op_get_directions);
        enter_otp = findViewById(R.id.pin_view);

        user = SharedPrefManager.getInstance(this).getUser();
        uid = user.getUSERID();
        opName = user.getF_NAME()+" "+user.getL_NAME();

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOtpAndSuccessAppt();
            }
        });

        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();

        cancel_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opCancelAppt();
            }

            private void opCancelAppt() {
                String reason = addreason.getEditText().getText().toString();

                class UserLogin extends AsyncTask<Void, Void, String> {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);


                        //converting response to json object

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(s);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Active_apnt_Operator.this, Home_Activity_Operator.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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

                        //returing the response
                        params.put("APPT_ID", apid);
                        params.put("OP_ID", uid);
                        params.put("REASON", reason);

                        return requestHandler.sendPostRequest(URLs.URL_OP_CANCEL_APPT, params);
                    }
                }

                UserLogin ul = new UserLogin();
                ul.execute();

            }
        });

        get_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+U_LATITUDE+","+U_LONGITUDE+"&mode=1"));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

                if(intent.resolveActivity(getPackageManager()) !=null){
                    startActivity(intent);
                }else{
                    Toast.makeText(Active_apnt_Operator.this, "Google Maps Not Found! Please Install Google MAps.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        call_citizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + S_CITIZEN_PHONE_NO));
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
    private void verifyOtpAndSuccessAppt() {
        String otp = enter_otp.getText().toString();
        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                //converting response to json object

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        /**
                         * SUCCESS ACCTIVITY
                        */
                        Intent intent = new Intent(Active_apnt_Operator.this, Home_Activity_Operator.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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

                //returing the response
                params.put("OP_ID", uid);
                params.put("APPT_ID", apid);
                params.put("OTP", otp);
                params.put("USERID", useridappt);
                params.put("OP_NAME", opName);

                Log.e("OP_ID", uid);
                Log.e("APPT_ID", apid);
                Log.e("OTP", otp);
                Log.e("USERID", useridappt);

                return requestHandler.sendPostRequest(URLs.URL_OP_VERIFY_APPT_OTP, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
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
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = obj.getJSONArray("Appointments");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            S_APPT_ID = jsonObject1.getString("APNT_ID");
                            S_APNT_PEND_DT = jsonObject1.getString("APNT_PEND_DT");
                            S_APNT_ACPT_DT = jsonObject1.getString("APNT_ACPT_DT");
                            S_APNT_DETAIL = jsonObject1.getString("APNT_DETAIL");
                            S_CITIZEN_F_NAME = jsonObject1.getString("F_NAME");
                            S_CITIZEN_L_NAME = jsonObject1.getString("L_NAME");
                            S_U_UPDATE_DETAILS = jsonObject1.getString("APNT_DETAIL");
                            S_U_ADDRESS = jsonObject1.getString("U_ADDRESS");
                            S_U_CITY = jsonObject1.getString("U_CITY");
                            S_U_STATE = jsonObject1.getString("U_STATE");
                            S_U_PINCODE = jsonObject1.getString("U_PINCODE");
                            S_CITIZEN_PHONE_NO = jsonObject1.getString("U_PHONE_NO");
                            U_LATITUDE = jsonObject1.getString("U_LATITUDE");
                            U_LONGITUDE = jsonObject1.getString("U_LONGITUDE");
                            useridappt = jsonObject1.getString("USERID");
                            apid = jsonObject1.getString("APNT_ID");
                        }

                        appt_id.setText("Appointment ID: "+S_APPT_ID);
                        citizen_name.setText(S_CITIZEN_F_NAME + " " + S_CITIZEN_L_NAME);
                        booked_dt.setText("Booked Date: "+S_APNT_PEND_DT);
                        accepted_dt.setText("Accepted Date: "+S_APNT_ACPT_DT);
                        update_details.setText("Details for Updation: "+S_U_UPDATE_DETAILS);
                        address.setText("Address: "+S_U_ADDRESS);
                        city.setText("City: "+S_U_CITY);
                        state.setText("State: "+S_U_STATE);
                        pincode.setText("Pincode: "+S_U_PINCODE);

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("OP_ID", uid);
                Log.e("aaa", uid);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_GET_ACTIVE_APPT, params);
            }
        }

        GetOpLocation ul = new GetOpLocation();
        ul.execute();
    }

}