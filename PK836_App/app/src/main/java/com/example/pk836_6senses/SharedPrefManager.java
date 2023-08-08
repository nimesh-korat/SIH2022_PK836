package com.example.pk836_6senses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.pk836_6senses.Citizen.Login_Page_Citizen;
import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.Operator.Login_Page_Operator;
import com.example.pk836_6senses.Operator.locationSave_Operator;

import java.util.Locale;
import java.util.Objects;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_ID = "key_id";
    private static final String KEY_F_NAME = "key_username";
    private static final String KEY_L_NAME = "key_usersurname";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_PASS = "key_pass";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_ADDRESS = "key_add";
    private static final String KEY_CITY = "key_city";
    private static final String KEY_STATE = "key_state";
    private static final String KEY_DOB = "key_dob";
    private static final String KEY_PINCODE = "key_pincode";
    private static final String KEY_GENDER = "key_gender";
    private static final String KEY_ROLE = "key_role";
    private static final String KEY_DP = "key_dp";
    private static final String KEY_DATE = "key_date";
    private static final String KEY_OP_LAT = "key_op_lat";
    private static final String KEY_OP_LONG = "key_op_long";
    private static final String KEY_LANGUALGE = "language";

    private static SharedPrefManager mInstance;
    private final SharedPreferences lang;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
        lang = mCtx.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void locationPref(locationSave_Operator locationSave_operator) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_OP_LAT, locationSave_operator.getOP_LATS());
        editor.putString(KEY_OP_LONG, locationSave_operator.getOP_LONGS());

        editor.apply();
    }

    public locationSave_Operator getSavedLocation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new locationSave_Operator(
                sharedPreferences.getString(KEY_OP_LAT, null),
                sharedPreferences.getString(KEY_OP_LONG, null)
        );
    }

    public String getLang(){
        return  lang.getString("lang", "en");
    }

    public void updateResource(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = mCtx.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setDefaultLang(code);
    }

    public void  setDefaultLang(String code){
        SharedPreferences.Editor editor = lang.edit();
        editor.putString("lang", code);
        editor.commit();
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUSERID());
        editor.putString(KEY_F_NAME, user.getF_NAME());
        editor.putString(KEY_L_NAME, user.getL_NAME());
        editor.putString(KEY_EMAIL, user.getEMAIL_ID());
        editor.putString(KEY_PASS, user.getPASSWORD());
        editor.putString(KEY_PHONE, user.getPHONE_NO());
        editor.putString(KEY_ADDRESS, user.getADDRESS());
        editor.putString(KEY_CITY, user.getCITY());
        editor.putString(KEY_STATE, user.getSTATE());
        editor.putString(KEY_PINCODE, user.getPINCODE());
        editor.putString(KEY_DOB, user.getDOB());
        editor.putString(KEY_GENDER, user.getGENDER());
        editor.putString(KEY_ROLE, user.getROLE());
        editor.putString(KEY_DP, user.getDP_FILE());
        editor.putString(KEY_DATE, user.getREG_DATE_TIME());

        editor.apply();
    }

    @SuppressLint("ApplySharedPref")
    public void update(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUSERID());
        editor.putString(KEY_F_NAME, user.getF_NAME());
        editor.putString(KEY_L_NAME, user.getL_NAME());
        editor.putString(KEY_EMAIL, user.getEMAIL_ID());
        editor.putString(KEY_PASS, user.getPASSWORD());
        editor.putString(KEY_PHONE, user.getPHONE_NO());
        editor.putString(KEY_ADDRESS, user.getADDRESS());
        editor.putString(KEY_CITY, user.getCITY());
        editor.putString(KEY_STATE, user.getSTATE());
        editor.putString(KEY_PINCODE, user.getPINCODE());
        editor.putString(KEY_DOB, user.getDOB());
        editor.putString(KEY_GENDER, user.getGENDER());
        editor.putString(KEY_ROLE, user.getROLE());
        editor.putString(KEY_DP, user.getDP_FILE());
        editor.putString(KEY_DATE, user.getREG_DATE_TIME());

        editor.commit();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_F_NAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_F_NAME, null),
                sharedPreferences.getString(KEY_L_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PASS, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_CITY, null),
                sharedPreferences.getString(KEY_STATE, null),
                sharedPreferences.getString(KEY_PINCODE, null),
                sharedPreferences.getString(KEY_DOB, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_ROLE, null),
                sharedPreferences.getString(KEY_DP, null),
                sharedPreferences.getString(KEY_DATE, null)
        );
    }

    //this method will logout the user
    public void logout() {

        if (Objects.equals(getUser().getROLE(), "CITIZEN")) {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(mCtx, Login_Page_Citizen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.startActivity(intent);
        } else {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(mCtx, Login_Page_Operator.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtx.startActivity(intent);
        }
    }
}