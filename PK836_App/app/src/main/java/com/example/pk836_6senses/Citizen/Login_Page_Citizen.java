package com.example.pk836_6senses.Citizen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.Guest.Guest_Book_Appt;
import com.example.pk836_6senses.Guest.Guest_Home_Activity;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.royrodriguez.transitionbutton.utils.WindowUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login_Page_Citizen extends AppCompatActivity {

    TextInputLayout phoneno, password;
    String phone, pass;
    MaterialButton btn_login,change_lang, guest_login_btn,forget_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        setContentView(R.layout.activity_login_page_citizen);
        //loadLocale();
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        //setContentView(R.layout.activity_login_page_citizen);
        WindowUtils.makeStatusbarTransparent(this);

        SharedPrefManager langs = new SharedPrefManager(this);
        phoneno = findViewById(R.id.login_phone_citizen);
        password = findViewById(R.id.login_password_citizen);
        btn_login = findViewById(R.id.btn_login_citizen);
        change_lang = findViewById(R.id.change_language);
        guest_login_btn = findViewById(R.id.guest_login);
        forget_pass = findViewById(R.id.forgetPass);

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Page_Citizen.this,ForgetPass.class);
                startActivity(intent);
            }
        });

        guest_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Login_Page_Citizen.this, Guest_Home_Activity.class);
                startActivity(intent);
            }
        });



        findViewById(R.id.txt_goto_reg_citizen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Login_Page_Citizen.this, register1.class));
            }
        });


/*
        change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] languages = {"English" , "Gujarati" , "Hindi"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login_Page_Citizen.this);
                mBuilder.setTitle("Choose Language");
                mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            langs.updateResource("en");
                            recreate();
                        }
                        else if (which == 1){
                            langs.updateResource("gu");
                            recreate();
                        }
                        else if (which == 2){
                            langs.updateResource("hi");
                            recreate();
                        }
                    }
                });
                mBuilder.show();
            }
        });
*/

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }


/*
    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration , getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings" , MODE_PRIVATE).edit();
        editor.putString("app_lang" , language);
        editor.apply();
    }
    private void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings" , MODE_PRIVATE);
        String language = preferences.getString("app_lang" , "");
        setLocale(language);
    }
*/

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(Login_Page_Citizen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
       // progressDialog.show();
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        userLogin();
                        // onLoginFailed();
                       // progressDialog.dismiss();
                    }
                }, 1000);
    }

    private void userLogin() {

        phoneno.getEditText().getText().toString();
        password.getEditText().getText().toString();
        final String role = "CITIZEN";



        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar_login_citizen);
                progressBar.setVisibility(View.VISIBLE);

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


                        Log.e("DP_FILE", userJson.getString("DP_FILE"));

                        //save in shared pref..
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        finish();
                        Intent Page = new Intent(Login_Page_Citizen.this, Home_Activity_Citizen.class);
                        startActivity(Page);

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
                params.put("PHONE_NO", phone);
                params.put("PASSWORD", pass);
                params.put("ROLE", role);

                //Log.e("pass", pass);
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