package com.example.pk836_6senses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pk836_6senses.Citizen.Login_Page_Citizen;
import com.example.pk836_6senses.Operator.Login_Page_Operator;

public class SelectUser extends AppCompatActivity {
    Button cit, ope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        cit = findViewById(R.id.gotoCitizen);
        ope = findViewById(R.id.gotoOperator);

        cit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, Login_Page_Citizen.class);
                startActivity(intent);
            }
        });

        ope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectUser.this, Login_Page_Operator.class);
                startActivity(intent);
            }
        });
    }
}