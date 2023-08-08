package com.example.pk836_6senses.Citizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.Operator.Op_register1;
import com.example.pk836_6senses.R;

public class PasswordUpdated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_updated);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), Op_register1.class);
        startActivity(intent);
    }
}