package com.example.pk836_6senses.No_Internet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.pk836_6senses.R;

public class NoInternet extends AppCompatActivity {

    LottieAnimationView noInternt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        noInternt= findViewById(R.id.no_inetrnet_anim);

        noInternt.playAnimation();
    }
}