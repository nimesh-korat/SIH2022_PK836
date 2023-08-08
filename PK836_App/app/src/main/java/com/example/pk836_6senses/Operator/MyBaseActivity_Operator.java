package com.example.pk836_6senses.Operator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.pk836_6senses.Citizen.Citizen_Settings;
import com.example.pk836_6senses.Citizen.MyBaseActivity_Citizen;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class MyBaseActivity_Operator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navView;
    ActionBarDrawerToggle toggle;
    LinearLayout contentView;

    BGLocationService_Operator mLocationService = new BGLocationService_Operator();
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_base_operator);

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

    private void stopServiceFunc(){
        mLocationService = new BGLocationService_Operator();
        mServiceIntent = new Intent(this, mLocationService.getClass());
        if (UtilForLocation_Operator.isMyServiceRunning(mLocationService.getClass(), this)) {
            stopService(mServiceIntent);
            Toast.makeText(this, "Service stopped!!", Toast.LENGTH_SHORT).show();
            //saveLocation(); // explore it by your self
        } else {
            Toast.makeText(this, "Service is already stopped!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.op_home) {
            drawer.setSelected(true);
            Intent Page = new Intent(MyBaseActivity_Operator.this, Home_Activity_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_attendance) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, AttendanceActivity_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_apnt_Pending) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, Pending_apnt_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_apnt_Completed) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, Completed_apnt_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_profile) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, Profile_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_resign) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, Resign_Activity_Operator.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.settings) {
            Intent Page = new Intent(MyBaseActivity_Operator.this, OperatorSettings.class);
            startActivity(Page);
            drawer.close();
        } else if (id == R.id.op_logout) {
            stopServiceFunc();
            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }
        return super.onOptionsItemSelected(item);

    }
}