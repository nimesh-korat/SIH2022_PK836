package com.example.pk836_6senses.Citizen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPass extends AppCompatActivity {
    TextInputLayout phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        phone = findViewById(R.id.forget_password_phone_number);

/*        Animation Hook
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);

Set animation to all the elements
        screenIcon.setAnimation(animation);
        title.setAnimation(animation);
        description.setAnimation(animation);
        phoneNumberTextField.setAnimation(animation);
        countryCodePicker.setAnimation(animation);
        nextBtn.setAnimation(animation);*/
    }

    public void verifyPhoneNumber(View view) {

        String phoneno = phone.getEditText().getText().toString();

        Log.e("sss", phoneno);

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
                        Intent Page = new Intent(ForgetPass.this, ForgetPassVerifyCode.class);
                        Page.putExtra("phone", phoneno);
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
                params.put("PHONE_NO", phoneno);
                Log.e("ssss", phoneno);

                //returing the response
                return requestHandler.sendPostRequest("http://192.168.1.6/6senses/otp_for_forget_pass.php", params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    public void callBackScreenFromForgetPassword(View view) {
    }
}