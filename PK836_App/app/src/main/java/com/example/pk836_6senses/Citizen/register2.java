package com.example.pk836_6senses.Citizen;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;

import java.util.Calendar;

public class register2 extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton selectedGender;
    Button next, login;
    DatePicker datePicker;
    String str_dob,str_gender, imagename, imagefile, mi;
    TextView signup_title_text,slideText;
    ImageView back,logo;
    String s_nm, s_surname, s_mail, s_pass,s_confirmPass, s_dob, s_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        setContentView(R.layout.activity_register2);

        signup_title_text = findViewById(R.id.signup_title_text);
        radioGroup= findViewById(R.id.citizen_gender);
        datePicker = findViewById(R.id.citizen_dob);

        next = findViewById(R.id.signup_next_button2);
        logo = findViewById(R.id.logo2);
        login = findViewById(R.id.signup_login_button);
        back = findViewById(R.id.signup_back_button);
        slideText = findViewById(R.id.signup_slide_text);
        datePicker.setMaxDate(System.currentTimeMillis() - 568025136000L);

        Intent intent = getIntent();

        s_nm =intent.getStringExtra("FNAME");
        s_surname =intent.getStringExtra("LNAME");
        s_mail =intent.getStringExtra("EMAIL");
        s_pass =intent.getStringExtra("PASSWORD");
        s_confirmPass =intent.getStringExtra("CONFIRM_PASS");
        imagename =intent.getStringExtra("imagename");
        imagefile =intent.getStringExtra("imagefile");
        mi =intent.getStringExtra("mi");

        Log.e("dvd",intent.getStringExtra("FNAME"));
        Log.e("dvd",intent.getStringExtra("LNAME"));
        Log.e("dvd", intent.getStringExtra("EMAIL"));
        Log.e("dvd",intent.getStringExtra("PASSWORD"));
        Log.e("dvd",intent.getStringExtra("CONFIRM_PASS"));


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register2.this,Login_Page_Citizen.class);
                startActivity(intent);
            }
        });
    }

    public void call3rdSigupScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), register3.class);

        if (!validateGender() | !validateAge()) {
            return;
        }

        selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
        selectedGender.getText();

        str_gender = selectedGender.getText().toString();


        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();


        str_dob = year+"-"+month+"-"+day;

        intent.putExtra("GENDER", str_gender);
        intent.putExtra("DOB", str_dob);
        intent.putExtra("FNAME", s_nm);
        intent.putExtra("LNAME", s_surname);
        intent.putExtra("EMAIL", s_mail);
        intent.putExtra("PASSWORD", s_pass);
        intent.putExtra("CONFIRM_PASS", s_confirmPass);
        intent.putExtra("imagename", imagename);
        intent.putExtra("imagefile", imagefile);
        intent.putExtra("mi", mi);


        Log.e("dvds",str_dob);
        Log.e("cd",str_gender);



        //Add Shared Animation
        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(logo, "transition_logo");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(signup_title_text, "transition_title_text");
        pairs[4] = new Pair<View, String>(slideText, "transition_slide_text");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(register2.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 18) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }
}