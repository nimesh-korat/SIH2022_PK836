package com.example.pk836_6senses.Operator;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.Citizen.Login_Page_Citizen;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login_Page_Operator extends AppCompatActivity {

    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    TextInputLayout phoneno, password;
    String phone, pass;
    MaterialButton btn_toRegister, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        setContentView(R.layout.activity_login_page_operator);

        phoneno = findViewById(R.id.login_phone_operator);
        password = findViewById(R.id.login_password_operator);

        btn_toRegister = findViewById(R.id.reg_ToLoginButton);
        btn_login = findViewById(R.id.login_button);


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
        btn_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Login_Page_Operator.this, Op_register1.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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
    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(Login_Page_Operator.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        userLogin();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }

    private void userLogin() {

        phoneno.getEditText().getText().toString();
        password.getEditText().getText().toString();
        final String role = "OPERATOR";

/*        //validating inputs
        if (TextUtils.isEmpty(phoneno)) {
            ph.setError("Please enter your phoneno");
            ph.requestFocus();
            return;
        }
        if (phoneno.length() < 10) {
            ph.setError("Enter a valid phone number");
            ph.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pass.setError("Please enter your password");
            pass.requestFocus();
            return;
        }*/

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar_login_operator);
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

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("USERID"),
                                userJson.getString("F_NAME"),
                                userJson.getString("L_NAME"),
                                userJson.getString("EMAIL_ID"),
                                userJson.getString("PASSWORD"),
                                userJson.getString("PHONE_NO"),
                                userJson.getString("ADDRESS"),
                                userJson.getString("CITY"),
                                userJson.getString("STATE"),
                                userJson.getString("PINCODE"),
                                userJson.getString("DOB"),
                                userJson.getString("GENDER"),
                                userJson.getString("ROLE"),
                                userJson.getString("DP_FILE"),
                                userJson.getString("REG_DATE_TIME")
                        );

                        //save in shared pref..
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        Intent Page = new Intent(Login_Page_Operator.this, Home_Activity_Operator.class);
                        startActivity(Page);

                    } else {

                        //Toast.makeText(getApplicationContext(), String.valueOf(obj.getBoolean("error")), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("PHONE_NO", phone);
                params.put("PASSWORD", pass);
                params.put("ROLE", role);

                // Log.e("pass", pass);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {

        boolean valid = true;
        phone = phoneno.getEditText().getText().toString();
        pass = password.getEditText().getText().toString();

        if (phone.isEmpty() || phone.length() < 10) {
            phoneno.setError("enter a valid phone number");
            valid = false;
        } else {
            phoneno.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 25) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
            requestFocus(password);
        } else {
            requestFocus(password);
            password.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //To prevent user to going to previous activity
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}