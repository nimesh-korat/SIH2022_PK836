package com.example.pk836_6senses.Citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pk836_6senses.R;
import com.github.barteksc.pdfviewer.PDFView;

public class Pdf_Citizen extends MyBaseActivity_Citizen {
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_pdf_citizen, null, false);
        drawer.addView(v, 0);

        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("aadhaar.pdf").load();
    }

}