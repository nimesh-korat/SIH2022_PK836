package com.example.pk836_6senses.No_Internet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pk836_6senses.Citizen.MyBaseActivity_Citizen;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;

public class NoOpFound extends MyBaseActivity_Citizen {

    LottieAnimationView noOperator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        setContentView(R.layout.activity_no_op_found);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_book_appt_on_portal, null, false);
        drawer.addView(v, 0);


        noOperator= findViewById(R.id.no_inetrnet_anim);

        noOperator.playAnimation();
    }
}