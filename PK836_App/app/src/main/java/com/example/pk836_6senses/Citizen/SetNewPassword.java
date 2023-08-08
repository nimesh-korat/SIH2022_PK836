package com.example.pk836_6senses.Citizen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SetNewPassword extends AppCompatActivity {
    Button btnUpdate;
    String ph;
    TextInputLayout newpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        btnUpdate = findViewById(R.id.set_new_password_btn);
        newpass = findViewById(R.id.new_password);


        Intent intent = getIntent();
        ph = intent.getStringExtra("phone");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });

    }

    private void updatePassword() {

        String pass = newpass.getEditText().getText().toString();

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
                        Intent Page = new Intent(SetNewPassword.this, PasswordUpdated.class);
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
                params.put("PASSWORD", pass);

                //returing the response
                return requestHandler.sendPostRequest("http://192.168.1.6/6senses/forget_password.php", params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }

    public void goToHomeFromSetNewPassword(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgetPass.class);
        startActivity(intent);
    }
}