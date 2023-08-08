package com.example.pk836_6senses.Operator;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pk836_6senses.R;
import com.example.pk836_6senses.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Op_register1 extends AppCompatActivity {
    TextInputLayout nm, surname, mail, pass, repass;
    String s_nm, s_surname, s_mail, s_pass, s_repass, imagename, imagefile, mi;
    Button next, login;
    ImageView back,op_dp;
    TextView signup_title_text,slideText;
    private int GALLERY = 1;
    String path = null, fileExtension, mime, imageName;
    File imageFile;
    String[] permissionRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_op_register1);

        // hooks
        if (!AskPermissions(this, permissionRequired)) {
            ActivityCompat.requestPermissions(this, permissionRequired, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Op_register1.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
        nm = findViewById(R.id.operator_name);
        signup_title_text = findViewById(R.id.signup_title_text);
        surname = findViewById(R.id.operator_surname);
        mail = findViewById(R.id.operator_mail);
        pass = findViewById(R.id.operator_pass);
        repass = findViewById(R.id.operator_repass);

        op_dp = findViewById(R.id.op_dp_regs);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        back = findViewById(R.id.signup_back_button);
        slideText = findViewById(R.id.signup_slide_text);

        op_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }/*
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery"};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }*/

    public static boolean AskPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery"};


        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(Op_register1.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    op_dp.setImageDrawable(null);
                    op_dp.setImageBitmap(bitmap);

                    path = getPath(contentURI);
                    imageFile = new File(path);
                    Uri uris = Uri.fromFile(imageFile);
                    fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
                    mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
                    imageName = imageFile.getName();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Op_register1.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void callNextSigupScreen(View view) {

        if (!validateFName() | !validateLName() | !validateEmail() | !validatePassword() | !validateRePassword()) {
            return;
        }
        s_nm = nm.getEditText().getText().toString();
        s_surname = surname.getEditText().getText().toString();
        s_mail = mail.getEditText().getText().toString();
        s_pass = pass.getEditText().getText().toString();
        s_repass = repass.getEditText().getText().toString();
        imagename = imageName;
        imagefile = String.valueOf(imageFile);
        mi = mime;

        Intent intent = new Intent(getApplicationContext(), Op_register2.class);

        intent.putExtra("FNAME", s_nm);
        intent.putExtra("LNAME", s_surname);
        intent.putExtra("EMAIL", s_mail);
        intent.putExtra("PASSWORD", s_pass);
        intent.putExtra("CONFIRM_PASS", s_repass);
        intent.putExtra("imagename", imagename);
        intent.putExtra("imagefile", imagefile);
        intent.putExtra("mi", mi);

        //Add Shared Animation
        Pair[] pairs = new Pair[4];
        // pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[0] = new Pair<View, String>(next, "transition_next_btn");
        pairs[1] = new Pair<View, String>(login, "transition_login_btn");
        pairs[2] = new Pair<View, String>(signup_title_text, "transition_title_text");
        pairs[3] = new Pair<View, String>(slideText, "transition_slide_text");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Op_register1.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void callLoginFromSignUp(View view) {
    }

    private Boolean validateFName() {

        String val = nm.getEditText().getText().toString();
        if (val.isEmpty()) {
            nm.setError("Field cannot be empty");
            return false;
        } else {
            nm.setError(null);
            return true;
        }
    }

    private Boolean validateLName() {

        String val = surname.getEditText().getText().toString();
        if (val.isEmpty()) {
            surname.setError("Field cannot be empty");
            return false;
        } else {
            surname.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = mail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+";

        if (val.isEmpty()) {
            mail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            mail.setError("Invalid email address");
            return false;
        } else {
            mail.setError(null);
            mail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = pass.getEditText().getText().toString();
        String passwordVal = "^" +
             /*   "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +  */       //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (password.isEmpty()) {
            pass.setError("Field cannot be empty");
            return false;
        } else if (pass.getEditText().getText().toString().length() <= 8) {
            pass.setError("Password must be greater then 8 digits");
            return false;
        } else if (!password.matches(passwordVal)) {
            pass.setError("Password is too weak");
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateRePassword() {

        String repassword = repass.getEditText().getText().toString();
        if (repassword.isEmpty()) {
            repass.setError("Re-Password is Empty");
            return false;
        } else if (!pass.getEditText().getText().toString().equals(repass.getEditText().getText().toString())) {
            repass.setError("passwords must be same");
            return false;
        } else {
            repass.setError(null);
            repass.setErrorEnabled(false);
            return true;
        }

    }

}