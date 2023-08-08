package com.example.pk836_6senses.Operator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pk836_6senses.Citizen.Citizen_Settings;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

public class OperatorSettings extends MyBaseActivity_Operator {

    MaterialButton btn_login,change_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_operator_settings, null, false);
        drawer.addView(v, 0);

        change_lang = findViewById(R.id.change_language);
        SharedPrefManager langs = new SharedPrefManager(this);

        change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] languages = {"English" , "Gujarati" , "Hindi"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OperatorSettings.this);
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

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}