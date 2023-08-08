package com.example.pk836_6senses.Operator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class Apnt_Detail_accept_activity extends MyBaseActivity_Operator {

    String name, apnt_id, op_id, str_f_name, str_l_name, str_phone, str_address, str_pincode, str_details, str_lat, str_long, userid, str_city, str_state, str_status, str_prnding_dt, str_accept_dt, str_completed_dt;
    TextView appt_id, f_name, u_city, u_state, u_phone_no, u_address, u_pincode, apnt_details, apnt_status, apnt_pending_date, apnt_accept_date, apnt_completed_date;
    Button btn_accept;

    User user;
    String u_id, time_stamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_apnt_detail_accept, null, false);
        drawer.addView(v, 0);

        user = SharedPrefManager.getInstance(this).getUser();
        u_id = String.valueOf(user.getUSERID());
        name = String.valueOf(user.getF_NAME() + " " + user.getL_NAME());

        appt_id = findViewById(R.id.pending_appt_id);
        f_name = findViewById(R.id.pending_appt_citizen_name);
        u_phone_no = findViewById(R.id.pending_appt_citizen_phone);
        u_address = findViewById(R.id.pending_appt_detail_address);
        u_city = findViewById(R.id.pending_appt_detail_city);
        u_state = findViewById(R.id.pending_appt_detail_state);
        u_pincode = findViewById(R.id.pending_appt_detail_pincode);
        apnt_details = findViewById(R.id.pending_appt_detail_detail);
        ;
        apnt_pending_date = findViewById(R.id.pending_appt_booked_date);
        btn_accept = findViewById(R.id.btn_accept);

        apnt_id = getIntent().getStringExtra("APNT_ID");
        userid = getIntent().getStringExtra("USERID");
        op_id = getIntent().getStringExtra("OP_ID");
        str_f_name = getIntent().getStringExtra("F_NAME");
        str_l_name = getIntent().getStringExtra("L_NAME");
        str_phone = getIntent().getStringExtra("U_PHONE_NO");
        str_address = getIntent().getStringExtra("U_ADDRESS");
        str_pincode = getIntent().getStringExtra("U_PINCODE");
        str_details = getIntent().getStringExtra("APNT_DETAIL");
        str_lat = getIntent().getStringExtra("LU_LATITUDEat");
        str_long = getIntent().getStringExtra("U_LONGITUDE");
        str_city = getIntent().getStringExtra("U_CITY");
        str_state = getIntent().getStringExtra("U_STATE");
        str_prnding_dt = getIntent().getStringExtra("APNT_PEND_DT");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        time_stamp = simpleDateFormat.format(new Date());

        appt_id.setText("Appointment ID : " + apnt_id);
        apnt_pending_date.setText("Booked Date : " + str_prnding_dt);
        f_name.setText("Name of Citizen :" + str_f_name + " " + str_l_name);
        u_phone_no.setText("Phone No: " + str_phone);
        apnt_details.setText("Details to be Upadte : " + str_details);
        u_address.setText("Address " + str_address);
        u_city.setText("City : " + str_city);
        u_state.setText("State : " + str_state);
        u_pincode.setText("Pincode : " + str_pincode);
        //apnt_status.setText(str_status);


/*        if (str_accept_dt.isEmpty()) {
            apnt_accept_date.setText(str_accept_dt);
            apnt_accept_date.setVisibility(View.VISIBLE);
        } else {
            apnt_accept_date.setVisibility(View.GONE);
        }

        if (str_completed_dt.isEmpty()) {
            apnt_completed_date.setText(str_completed_dt);
            apnt_completed_date.setVisibility(View.VISIBLE);
        } else {
            apnt_completed_date.setVisibility(View.GONE);
        }*/

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept_apnt();
                Log.e("date", time_stamp);
            }
        });
    }

    private void accept_apnt() {
        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = findViewById(R.id.progressBar_login_operator);
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

                        Intent Page = new Intent(Apnt_Detail_accept_activity.this, Home_Activity_Operator.class);
                        startActivity(Page);

                    } else {

                        //Toast.makeText(getApplicationContext(), String.valueOf(obj.getBoolean("error")), Toast.LENGTH_SHORT).show();
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
                params.put("APNT_ID", apnt_id);
                params.put("OP_ID", u_id);
                params.put("OP_NAME", name);

                Log.e("APNT_ID", apnt_id);
                Log.e("OP_ID", u_id);
                Log.e("APNT_STATUS", "ACCEPTED");
                Log.e("APNT_ACPT_DT", time_stamp);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_ACCEPT_ACPT, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}

