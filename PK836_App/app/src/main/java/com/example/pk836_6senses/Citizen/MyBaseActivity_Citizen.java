package com.example.pk836_6senses.Citizen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class MyBaseActivity_Citizen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawer;
    protected Toolbar toolbar;
    NavigationView navView;
    ActionBarDrawerToggle toggle;
    LinearLayout contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        setContentView(R.layout.activity_my_base);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.navView);
        contentView = findViewById(R.id.content);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        naviagtionDrawer();



    }

    private void naviagtionDrawer() {
        //Naviagtion Drawer
        navView.bringToFront();
        navView.setNavigationItemSelectedListener(this);
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - 0.7f);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent Page = new Intent(MyBaseActivity_Citizen.this, Home_Activity_Citizen.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.apnt_Pending) {
            Intent Page = new Intent(MyBaseActivity_Citizen.this, Pending_apnt_Citizen.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.apnt_Completed) {
            Intent Page = new Intent(MyBaseActivity_Citizen.this, Completed_apnt_Citizen.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.profile) {
            Intent Page = new Intent(MyBaseActivity_Citizen.this, Profile_Citizen.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.settings) {
            Intent Page = new Intent(MyBaseActivity_Citizen.this, Citizen_Settings.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.logout) {
            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
        return super.onOptionsItemSelected(item);

    }
}