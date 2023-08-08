package com.example.pk836_6senses.Citizen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.chaos.view.PinView;
import com.example.pk836_6senses.Citizen.Add_Appointment;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_Appointment extends MyBaseActivity_Citizen {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    TextView username, city, state;
    RadioGroup rg;
    RadioButton rb_new, rb_update;
    CheckBox cb_nm_update, cb_add_update, cb_mobile_update, cb_email_update, cb_photo_update, cb_dob_update, cb_gender_update;
    Button btn_submit, btn_get_pin,btn_fname,btn_lname,btn_phone,btn_address,btn_pincode, send_otp, verify_otp;
    ProgressBar progressBar;
    TextToSpeech textToSpeech;
    User user;
    String nm, snm, str_phone, str_address, str_pincode, needToSolve, str_lat, str_long, userid, str_city, str_state, str_fname, str_lname;
    TextInputLayout phone, address, pincode, fname, lname;
    PinView otp;
    SmsVerifyCatcher smsVerifyCatcher;
    private LocationManager locationManager;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_SPEECH_INPUT1 = 2;
    private static final int REQUEST_CODE_SPEECH_INPUT2 = 3;
    private static final int REQUEST_CODE_SPEECH_INPUT3 = 4;
    private static final int REQUEST_CODE_SPEECH_INPUT4 = 5;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_add_appointment, null, false);
        drawer.addView(v, 0);

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        btn_fname = findViewById(R.id.fname_speech);
        btn_lname = findViewById(R.id.last_speech);
        btn_phone = findViewById(R.id.phoneNo_speech);
        btn_address = findViewById(R.id.address_speech);
        btn_pincode = findViewById(R.id.pincode_speech);
        otp = findViewById(R.id.show_otp_appt);
        verify_otp = findViewById(R.id.verify_otp_phone);
        send_otp = findViewById(R.id.send_otp_phone);
        fname = findViewById(R.id.add_apnt_fname);
        lname = findViewById(R.id.add_apnt_lname);
        username = findViewById(R.id.u_name_book);
        phone = findViewById(R.id.add_apnt_phone);
        address = findViewById(R.id.add_apnt_address);
        pincode = findViewById(R.id.add_apnt_pincode);
        rg = findViewById(R.id.rg_add_apnt);
        rb_new = findViewById(R.id.rb_new_enroll);
        rb_update = findViewById(R.id.rb_update_enroll);
        cb_nm_update = findViewById(R.id.cb_nm_update);
        cb_add_update = findViewById(R.id.cb_add_update);
        cb_mobile_update = findViewById(R.id.cb_mb_update);
        cb_email_update = findViewById(R.id.cb_em_update);
        cb_photo_update = findViewById(R.id.cb_photo_update);
        cb_dob_update = findViewById(R.id.cb_dob_update);
        cb_gender_update = findViewById(R.id.cb_gndr_update);
        btn_submit = findViewById(R.id.btn_book_apnt);
       // btn_cnfm_loc = findViewById(R.id.btn_cnfm_location);
        btn_get_pin = findViewById(R.id.btn_add_apnt_get_pincode);
        progressBar = findViewById(R.id.progressBar_signup_operator);
        city = findViewById(R.id.add_apnt_city);
        state = findViewById(R.id.add_apnt_state);

        user = SharedPrefManager.getInstance(this).getUser();
        nm = String.valueOf(user.getF_NAME());
        userid = String.valueOf(user.getUSERID());

        snm = String.valueOf(user.getL_NAME());

        str_lat = getIntent().getStringExtra("Lat");
        str_long = getIntent().getStringExtra("Long");

        btn_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast.makeText(Add_Appointment.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Add_Appointment.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpsend();
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
                    Toast.makeText(Add_Appointment.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                otp.setText(code);//set code in edit text
                //then you can send verification code to server
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
                    Toast.makeText(Add_Appointment.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Add_Appointment.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
/*
        //text to speech
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                fname.getEditText().setSelection(fname.getEditText().getText().length());
                //fullmsg.setText(matches.get(0));
                fname.getEditText().setText(fname.getEditText().getText().toString()+" "+matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.fname_speech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
        });*/

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
/*        btn_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = fname.getEditText().getText().toString();
                //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });*/


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        //getCurruntLocation();
        username.setText(nm + " " + snm);

        btn_get_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpincode();
            }
        });
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyotp();
            }
        });

        /*btn_cnfm_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Appointment.this, SelectLocationActivity.class);
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

                    //Toast.makeText(Add_Appointment.this, "Please Select What need to do!!!", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(Add_Appointment.this, "Please Confirm Location", Toast.LENGTH_LONG).show();
        }
    }
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
    private void verifyotp() {

        String str_phone = phone.getEditText().getText().toString();
        String str_otp = otp.getText().toString();

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
                    if(!obj.getBoolean("error")) {

                        btn_submit.setVisibility(View.VISIBLE);
                        send_otp.setVisibility(View.GONE);


                        Toast.makeText(Add_Appointment.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Add_Appointment.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("PHONE_NO", str_phone);
                params.put("OTP", str_otp);

                return requestHandler.sendPostRequest(URLs.URL_VERIFY_REG_OTP, params);

            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void otpsend() {
        String str_phone = phone.getEditText().getText().toString();

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
                    if(!obj.getBoolean("error")) {


                        verify_otp.setVisibility(View.VISIBLE);
                        otp.setVisibility(View.VISIBLE);
                        Toast.makeText(Add_Appointment.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Add_Appointment.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("PHONE_NO", str_phone);

                return requestHandler.sendPostRequest(URLs.URL_GET_REG_OTP, params);

            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
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

        @SuppressLint("StaticFieldLeak")
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
                        Intent Page = new Intent(Add_Appointment.this, Home_Activity_Citizen.class);
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
                params.put("USERID", userid);
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
                params.put("APNT_STATUS", "PENDING");

                Log.e("USERID", userid);
                Log.e("U_PHONE_NO", str_phone);
                Log.e("U_ADDRESS", str_address);
                Log.e("U_CITY", str_city);
                Log.e("U_STATE", str_state);
                Log.e("U_PINCODE", str_pincode);
                Log.e("U_LATITUDE", str_lat);
                Log.e("U_LONGITUDE", str_long);
                Log.e("APNT_DETAIL", needToSolve);
                Log.e("APNT_STATUS", "PENDING");

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_U_BOOK_APNT, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
/*
    private void getCurruntLocation() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder locationRequestBuilder = new LocationSettingsRequest.Builder();
        locationRequestBuilder.addLocationRequest(locationRequest);
        locationRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationRequestBuilder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(Add_Appointment.this, "GPS IS ON", Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Appointment.this, "GPS IS OFF", Toast.LENGTH_LONG).show();

                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(Add_Appointment.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

    }*/
}