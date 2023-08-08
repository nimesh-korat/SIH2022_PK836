package com.example.pk836_6senses.Citizen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Profile_Citizen extends MyBaseActivity_Citizen implements AdapterView.OnItemSelectedListener {

    String uid, str_dp, str_dp_path, str_fname, str_lname, str_email, str_phone, str_address, str_pin, str_dob, str_gender, str_city, str_state, str_pass, str_role, str_reg_date, str_select_gender;
    TextInputLayout fname, lname, email, phone, address, pincode, dob;
    TextView name;
    Spinner gender;
    Button btn_update, btn_pincode;
    ProgressBar progressBar;
    ImageView dp;
    String[] gender_list = {"Male", "Female", "Other"};
    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_profile_citizen, null, false);
        drawer.addView(v, 0);

        fname = findViewById(R.id.citizen_fname_profile);
        name = findViewById(R.id.citizen_fullname_profile);
        dp = findViewById(R.id.citizen_profile_image);
        lname = findViewById(R.id.citizen_lname_profile);
        email = findViewById(R.id.citizen_email_profile);
        phone = findViewById(R.id.citizen_phone_profile);
        address = findViewById(R.id.citizen_address_profile);
        pincode = findViewById(R.id.citizen_pin_profile);
        dob = findViewById(R.id.citizen_dob_profile);
        gender = findViewById(R.id.citizen_gender_profile);
        btn_update = findViewById(R.id.btn_citizen_update_profile);
        btn_pincode = findViewById(R.id.btn_citizen_get_pin_profile);
        progressBar = findViewById(R.id.progressBar_profile_citizen);

        str_dp_path = "http://172.17.87.229/6senses/images/";


        User user = SharedPrefManager.getInstance(this).getUser();
        uid = user.getUSERID();
        str_fname = user.getF_NAME();
        str_lname = user.getL_NAME();
        str_email = user.getEMAIL_ID();
        str_phone = user.getPHONE_NO();
        str_address = user.getADDRESS();
        str_pin = user.getPINCODE();
        str_dob = user.getDOB();
        str_gender = user.getGENDER();
        str_role = user.getROLE();
        str_dp = user.getDP_FILE();
        str_reg_date = user.getREG_DATE_TIME();

        String merged_path = str_dp_path + str_dp;
        Log.e("dfdds", merged_path);
        Glide.with(Profile_Citizen.this).load(merged_path).into(dp);

        name.setText(new StringBuilder().append(str_fname).append(" ").append(str_lname).toString());
        Objects.requireNonNull(fname.getEditText()).setText(str_fname);
        Objects.requireNonNull(lname.getEditText()).setText(str_lname);
        Objects.requireNonNull(email.getEditText()).setText(str_email);
        Objects.requireNonNull(phone.getEditText()).setText(str_phone);
        Objects.requireNonNull(address.getEditText()).setText(str_address);
        Objects.requireNonNull(pincode.getEditText()).setText(str_pin);
        Objects.requireNonNull(dob.getEditText()).setText(str_dob);

        gender.setOnItemSelectedListener(this);

        ArrayAdapter gs = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender_list);
        gs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gs);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (str_gender.equals("Male")) {
            gender.setSelection(0);
        } else if (str_gender.equals("Female")) {
            gender.setSelection(1);
        } else {
            gender.setSelection(2);
        }
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        dob.setClickable(true);
        dob.setFocusable(false);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Profile_Citizen.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
                        str_dob = year + "-" + month + "-" + day;
                        dob.getEditText().setText(date);
                        Log.e("vvv", str_dob);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 568025136000L);
                datePickerDialog.show();
            }
        });

        btn_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpincode();
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

    private void getpincode() {
        String str_pin = pincode.getEditText().getText().toString();

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

                    str_state = obj.getString("state");
                    str_city = obj.getString("city");

                    Toast.makeText(Profile_Citizen.this, "Pin-Code Verified Successfully", Toast.LENGTH_SHORT).show();

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
                params.put("pincode", str_pin);
                return requestHandler.sendPostRequest(URLs.URL_GET_PIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        str_select_gender = gender_list[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateProfile() {
        String u_fname = fname.getEditText().getText().toString();
        String u_lname = lname.getEditText().getText().toString();
        String u_email = email.getEditText().getText().toString();
        String u_phone = phone.getEditText().getText().toString();
        String u_address = address.getEditText().getText().toString();
        String u_pin = pincode.getEditText().getText().toString();

        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    progressBar.setVisibility(View.GONE);


                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        User userupdate = new User(
                                uid,
                                u_fname,
                                u_lname,
                                u_email,
                                str_pass,
                                u_phone,
                                u_address,
                                str_city,
                                str_state,
                                u_pin,
                                str_dob,
                                str_select_gender,
                                str_role,
                                str_dp,
                                str_reg_date
                        );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userupdate);

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent Page = new Intent(Profile_Citizen.this, Home_Activity_Citizen.class);
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
                params.put("F_NAME", u_fname);
                params.put("L_NAME", u_lname);
                params.put("EMAIL_ID", u_email);
                params.put("PHONE_NO", u_phone);
                params.put("ADDRESS", u_address);
                params.put("DOB", str_dob);
                params.put("GENDER", str_select_gender);
                params.put("PINCODE", u_pin);
                params.put("CITY", str_city);
                params.put("STATE", str_state);
                params.put("USERID", uid);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_UPDATE_PROFILE, params);
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();
    }
}