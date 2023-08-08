package com.example.pk836_6senses.Guest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pk836_6senses.Citizen.Add_Appointment;
import com.example.pk836_6senses.Citizen.Home_Activity_Citizen;
import com.example.pk836_6senses.Guest.Guest_Book_Appt;
import com.example.pk836_6senses.Citizen.MyBaseActivity_Citizen;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Guest_Book_Appt extends MyBaseActivity_Citizen {


    BroadcastReceiver broadcastReceiver = null;
    TextView city, state;
    TextView username;
    RadioGroup rg;
    RadioButton rb_new, rb_update;
    CheckBox cb_nm_update, cb_add_update, cb_mobile_update, cb_email_update, cb_photo_update, cb_dob_update, cb_gender_update;
    Button btn_submit, btn_get_pin, btn_fname, btn_lname, btn_phone, btn_address, btn_pincode;
    ProgressBar progressBar;
    TextToSpeech textToSpeech;
    User user;
    String nm, snm, str_phone, str_address, str_pincode, needToSolve, str_lat, str_long, abcd, userid, str_city, str_state, str_fname, str_lname, latitude, longitude;
    TextInputLayout phone, address, pincode, fname, lname;
    private LocationManager locationManager;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_SPEECH_INPUT1 = 2;
    private static final int REQUEST_CODE_SPEECH_INPUT2 = 3;
    private static final int REQUEST_CODE_SPEECH_INPUT3 = 4;
    private static final int REQUEST_CODE_SPEECH_INPUT4 = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_guest_book_appt, null, false);
        drawer.addView(v, 0);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

       /* LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/

       /* Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         longitude = String.valueOf(location.getLongitude());
         latitude = String.valueOf(location.getLatitude());*/

        btn_fname = findViewById(R.id.fname_speech_guest);
        btn_lname = findViewById(R.id.last_speech_guest);
        btn_phone = findViewById(R.id.phoneNo_speech_guest);
        btn_address = findViewById(R.id.address_speech_guest);
        btn_pincode = findViewById(R.id.pincode_speech_guest);

        fname = findViewById(R.id.add_apnt_fname_guest);
        lname = findViewById(R.id.add_apnt_lname_guest);
        phone = findViewById(R.id.add_apnt_phone_guest);
        address = findViewById(R.id.add_apnt_address_guest);
        pincode = findViewById(R.id.add_apnt_pincode_guest);
        rg = findViewById(R.id.rg_add_apnt_guest);
        rb_new = findViewById(R.id.rb_new_enroll_guest);
        rb_update = findViewById(R.id.rb_update_enroll_guest);
        cb_nm_update = findViewById(R.id.cb_nm_update_guest);
        cb_add_update = findViewById(R.id.cb_add_update_guest);
        cb_mobile_update = findViewById(R.id.cb_mb_update_guest);
        cb_email_update = findViewById(R.id.cb_em_update_guest);
        cb_photo_update = findViewById(R.id.cb_photo_update_guest);
        cb_dob_update = findViewById(R.id.cb_dob_update_guest);
        cb_gender_update = findViewById(R.id.cb_gndr_update_guest);
        btn_submit = findViewById(R.id.btn_book_apnt_guest);
        // btn_cnfm_loc = findViewById(R.id.btn_cnfm_location);
        btn_get_pin = findViewById(R.id.btn_add_apnt_get_pincode_guest);
        progressBar = findViewById(R.id.progressBar_signup_operator_guest);
        city = findViewById(R.id.add_apnt_city_guest);
        state = findViewById(R.id.add_apnt_state_guest);

        abcd = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("abcd", abcd);


        //str_lat = getIntent().getStringExtra("Lat");
        //str_long = getIntent().getStringExtra("Long");
        str_lat = "17.47533506069835";
        str_long = "78.72127879632755";

        btn_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast.makeText(Guest_Book_Appt.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT1);
                } catch (Exception e) {
                    Toast.makeText(Guest_Book_Appt.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT2);
                } catch (Exception e) {
                    Toast.makeText(Guest_Book_Appt.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT3);
                } catch (Exception e) {
                    Toast.makeText(Guest_Book_Appt.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT4);
                } catch (Exception e) {
                    Toast.makeText(Guest_Book_Appt.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        //getCurruntLocation();
        //username.setText(nm + " " + snm);

        btn_get_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpincode();
            }
        });

        /*btn_cnfm_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Guest_Book_Appt.this, SelectLocationActivity.class);
                startActivity(intent);
            }
        });*/

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (rb_update.isChecked()) {
                    cb_nm_update.setVisibility(View.VISIBLE);
                    cb_add_update.setVisibility(View.VISIBLE);
                    cb_mobile_update.setVisibility(View.VISIBLE);
                    cb_email_update.setVisibility(View.VISIBLE);
                    cb_photo_update.setVisibility(View.VISIBLE);
                    cb_dob_update.setVisibility(View.VISIBLE);
                    cb_gender_update.setVisibility(View.VISIBLE);

                } else {

                    for (CheckBox checkBox : Arrays.asList(cb_nm_update, cb_add_update, cb_mobile_update, cb_email_update, cb_photo_update, cb_dob_update, cb_gender_update)) {
                        checkBox.setChecked(false);
                    }

                    cb_nm_update.setVisibility(View.GONE);
                    cb_add_update.setVisibility(View.GONE);
                    cb_mobile_update.setVisibility(View.GONE);
                    cb_email_update.setVisibility(View.GONE);
                    cb_photo_update.setVisibility(View.GONE);
                    cb_dob_update.setVisibility(View.GONE);
                    cb_gender_update.setVisibility(View.GONE);

                }
            }

        });


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();

        if (str_lat != null && str_long != null) {
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    needToSolve = "";
                    if (rb_new.isChecked()) {
                        needToSolve += rb_new.getText().toString();
                    }
                    if (cb_nm_update.isChecked()) {
                        needToSolve += " - " + cb_nm_update.getText().toString();
                    }
                    if (cb_add_update.isChecked()) {
                        needToSolve += " - " + cb_add_update.getText().toString();
                    }
                    if (cb_mobile_update.isChecked()) {
                        needToSolve += " - " + cb_mobile_update.getText().toString();
                    }
                    if (cb_email_update.isChecked()) {
                        needToSolve += " - " + cb_email_update.getText().toString();
                    }
                    if (cb_photo_update.isChecked()) {
                        needToSolve += " - " + cb_photo_update.getText().toString();
                    }
                    if (cb_dob_update.isChecked()) {
                        needToSolve += " - " + cb_dob_update.getText().toString();
                    }
                    if (cb_gender_update.isChecked()) {
                        needToSolve += " - " + cb_gender_update.getText().toString();
                    }

                    bookAppointment();

                    //Toast.makeText(Guest_Book_Appt.this, "Please Select What need to do!!!", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(Guest_Book_Appt.this, "Please Confirm Location", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                fname.getEditText().setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
        if (requestCode == REQUEST_CODE_SPEECH_INPUT1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                lname.getEditText().setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
        if (requestCode == REQUEST_CODE_SPEECH_INPUT2) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                phone.getEditText().setText(
                        Objects.requireNonNull(result).get(0));

            }
        }
        if (requestCode == REQUEST_CODE_SPEECH_INPUT3) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                address.getEditText().setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
        if (requestCode == REQUEST_CODE_SPEECH_INPUT4) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                pincode.getEditText().setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
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

                    String ss = obj.getString("state");
                    String sd = obj.getString("city");
                    state.setText(ss);
                    city.setText(sd);
                    city.setVisibility(View.VISIBLE);
                    state.setVisibility(View.VISIBLE);
                    btn_submit.setEnabled(true);

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

    private void bookAppointment() {

        str_fname = fname.getEditText().getText().toString();
        str_lname = lname.getEditText().getText().toString();
        str_phone = phone.getEditText().getText().toString();
        str_address = address.getEditText().getText().toString();
        str_pincode = pincode.getEditText().getText().toString();
        str_city = city.getText().toString();
        str_state = state.getText().toString();

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

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent Page = new Intent(Guest_Book_Appt.this, Guest_Home_Activity.class);
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
                params.put("ANDROID_ID", abcd);
                params.put("F_NAME", str_fname);
                params.put("L_NAME", str_lname);
                params.put("U_PHONE_NO", str_phone);
                params.put("U_ADDRESS", str_address);
                params.put("U_CITY", str_city);
                params.put("U_STATE", str_state);
                params.put("U_PINCODE", str_pincode);
                params.put("U_LATITUDE", str_lat);
                params.put("U_LONGITUDE", str_long);
                params.put("APNT_DETAIL", needToSolve);

                Log.e("ANDROID_ID", abcd);
                Log.e("F_NAME", str_fname);
                Log.e("L_NAME", str_lname);
                Log.e("U_PHONE_NO", str_phone);
                Log.e("U_ADDRESS", str_address);
                Log.e("U_CITY", str_city);
                Log.e("U_STATE", str_state);
                Log.e("U_PINCODE", str_pincode);
                Log.e("U_LATITUDE", str_lat);
                Log.e("U_LONGITUDE", str_long);
                Log.e("APNT_DETAIL", needToSolve);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_GUEST_BOOK_APPT, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}