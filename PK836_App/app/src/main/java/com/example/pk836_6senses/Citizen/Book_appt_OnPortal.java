package com.example.pk836_6senses.Citizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;

public class Book_appt_OnPortal extends MyBaseActivity_Citizen {
    WebView web;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_book_appt_on_portal, null, false);
        drawer.addView(v, 0);

        web = findViewById(R.id.webview);
        textView = findViewById(R.id.textView);


        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.getSettings().setSupportMultipleWindows(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        web.setWebViewClient(new WebViewClient());
        web.loadUrl("https://ask.uidai.gov.in/#/");

        /*web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                textView.setText("Page loading : " + newProgress + "%");
                if (newProgress == 100) {
                    textView.setText("Page Loaded.");
                }
            }
        });*/

 /*       web.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                // progressBar.setVisibility(View.VISIBLE);
                //progressBar.setProgress(newProgress);

                if (newProgress == 100) {
                    //progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });*/


    }
}