package com.example.pk836_6senses.Operator;

import static com.example.pk836_6senses.Citizen.Comp_Appt_Details_Citizen.appt_id;
import static com.example.pk836_6senses.Citizen.Comp_Appt_Details_Citizen.op_IDS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Resign_Activity_Operator extends MyBaseActivity_Operator {

    TextView name, phone, email;
    TextInputLayout reason;
    CheckBox confirm_check;
    Button submit_btn;
    User user;
    String uid, s_reason;

    //For No internet
    BroadcastReceiver broadcastReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_resign_operator, null, false);
        drawer.addView(v, 0);

        name= findViewById(R.id.username_resign);
        phone= findViewById(R.id.contact_resign);
        email= findViewById(R.id.email_resign);
        reason= findViewById(R.id.comments_resign);
        confirm_check= findViewById(R.id.check_resign);
        submit_btn= findViewById(R.id.btn_cnf_resign);

        user = SharedPrefManager.getInstance(this).getUser();
        uid = String.valueOf(user.getUSERID());
        name.setText(user.getF_NAME()+ " "+ user.getL_NAME());
        phone.setText(user.getPHONE_NO());
        email.setText(user.getEMAIL_ID());

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestResign();
            }
        });


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
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
    private void requestResign() {

        s_reason = reason.getEditText().getText().toString();

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

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

                        Toast.makeText(Resign_Activity_Operator.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Resign_Activity_Operator.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("OP_ID", uid);
                params.put("RESIGN_REASON", s_reason);

                Log.e("OP_ID", uid);
                Log.e("RESIGN_REASON",s_reason);

                return requestHandler.sendPostRequest(URLs.URL_OP_ADD_RESIGN, params);

            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}