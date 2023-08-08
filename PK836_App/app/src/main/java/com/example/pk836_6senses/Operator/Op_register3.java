package com.example.pk836_6senses.Operator;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Op_register3 extends AppCompatActivity {
    TextInputLayout address, pincode;
    Button next, login, getPin;
    ImageView back;
    ProgressBar progressBar;
    TextView signup_title_text, slideText, city, state;
    String s_nm, s_surname, str_dob, str_gender, s_mail, s_pass, s_confirmPass,str_city,str_state, imagename, imagefile, mi;
    String str_address, str_pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_op_register3);

        signup_title_text = findViewById(R.id.signup_title_text);
        address = findViewById(R.id.operator_address);
        pincode = findViewById(R.id.operator_pincode);
        getPin = findViewById(R.id.btn_operator_get_pin);

        next = findViewById(R.id.signup_next_button3);
        login = findViewById(R.id.signup_login_button);
        back = findViewById(R.id.signup_back_button);
        slideText = findViewById(R.id.signup_slide_text);
        city = findViewById(R.id.operator_city);
        state = findViewById(R.id.operator_state);

        getPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpincode();
            }
        });

        Intent intent = getIntent();

        s_nm =intent.getStringExtra("FNAME");
        s_surname =intent.getStringExtra("LNAME");
        s_mail =intent.getStringExtra("EMAIL");
        s_pass =intent.getStringExtra("PASSWORD");
        s_confirmPass =intent.getStringExtra("CONFIRM_PASS");
        str_dob =intent.getStringExtra("DOB");
        str_gender =intent.getStringExtra("GENDER");
        imagename =intent.getStringExtra("imagename");
        imagefile =intent.getStringExtra("imagefile");
        mi =intent.getStringExtra("mi");

        Log.e("vfv", intent.getStringExtra("GENDER"));
        Log.e("fv", intent.getStringExtra("DOB"));


    }

    private void getpincode() {
        String str_pin = pincode.getEditText().getText().toString();

        Log.e("ss", str_pin);
        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    String ss = obj.getString("state");
                    String sd = obj.getString("city");
                    state.setText(ss);
                    city.setText(sd);

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

    public void call4rdSigupScreen(View view) {

        Intent intent = new Intent(getApplicationContext(), Op_register4.class);

        if (!validateAddress() | !validatePincode()) {
            return;
        }

        str_address = address.getEditText().getText().toString();
        str_pincode = pincode.getEditText().getText().toString();
        str_city = city.getText().toString();
        str_state = state.getText().toString();

        intent.putExtra("ADDRESS", str_address);
        intent.putExtra("PINCODE", str_pincode);
        intent.putExtra("CITY", str_city);
        intent.putExtra("STATE", str_state);
        intent.putExtra("GENDER", str_gender);
        intent.putExtra("DOB", str_dob);
        intent.putExtra("FNAME", s_nm);
        intent.putExtra("LNAME", s_surname);
        intent.putExtra("EMAIL", s_mail);
        intent.putExtra("PASSWORD", s_pass);
        intent.putExtra("imagename", imagename);
        intent.putExtra("imagefile", imagefile);
        intent.putExtra("mi", mi);

        //Add Shared Animation
        Pair[] pairs = new Pair[4];
        // pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[0] = new Pair<View, String>(next, "transition_next_btn");
        pairs[1] = new Pair<View, String>(login, "transition_login_btn");
        pairs[2] = new Pair<View, String>(signup_title_text, "transition_title_text");
        pairs[3] = new Pair<View, String>(slideText, "transition_slide_text");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Op_register3.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }


    private Boolean validateAddress() {

        String val = address.getEditText().getText().toString();
        if (val.isEmpty()) {
            address.setError("Field cannot be empty");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private Boolean validatePincode() {

        String val = address.getEditText().getText().toString();
        if (val.isEmpty()) {
            address.setError("Pincode cannot be empty");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

}