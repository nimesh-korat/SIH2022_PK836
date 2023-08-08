package com.example.pk836_6senses.Citizen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPassVerifyCode extends AppCompatActivity {
    String ph;
    Button btnVerify;
    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_verify_code);
        btnVerify = findViewById(R.id.verifyotp);
        pinView =  findViewById(R.id.pin_view);

        Intent intent = getIntent();
        ph = intent.getStringExtra("phone");

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyotp();
            }
        });

    }

    public void goToHomeFromOTP(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgetPass.class);
        startActivity(intent);
    }


    public void verifyotp() {

        String Str_pinView = pinView.getText().toString();


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
                        Intent Page = new Intent(ForgetPassVerifyCode.this, SetNewPassword.class);
                        Page.putExtra("phone", ph);
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
                params.put("PHONE_NO", ph);
                params.put("OTP", Str_pinView);
                Log.e("ph", ph);

                //returing the response
                return requestHandler.sendPostRequest("http://192.168.1.6/6senses/verify_otp_for_forget_pass.php", params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

}