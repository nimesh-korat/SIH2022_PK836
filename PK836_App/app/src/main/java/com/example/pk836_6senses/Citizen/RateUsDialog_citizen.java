package com.example.pk836_6senses.Citizen;

import static com.example.pk836_6senses.Citizen.Comp_Appt_Details_Citizen.appt_id;
import static com.example.pk836_6senses.Citizen.Comp_Appt_Details_Citizen.op_IDS;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RateUsDialog_citizen extends Dialog {

    public static Float userRateBehaviour;
    public static Float userRateAccuracy;
    User user;
    String uid;
    AppCompatButton rateNowBtn, laterBtn;
    RatingBar ratingBarBehaviour, ratingBarAccuracy;
    ImageView ratingImage;
    TextView rateTitle, rateDescription;
    TextInputLayout comments;
    View bg;

    public RateUsDialog_citizen(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
        sharedPrefManager.updateResource(sharedPrefManager.getLang());
        setContentView(R.layout.rate_us_dialog_layout_citizen);

        rateNowBtn = findViewById(R.id.rateNowBtn);
        //laterBtn = findViewById(R.id.laterBtn);
        ratingBarBehaviour = findViewById(R.id.ratingBarBehaviour);
        ratingBarAccuracy = findViewById(R.id.ratingBarAccuracy);
        ratingImage = findViewById(R.id.ratingImage);
        rateTitle = findViewById(R.id.rate_title);
        rateDescription = findViewById(R.id.rate_description);
        comments = findViewById(R.id.Comments);
        bg = findViewById(R.id.background);

        user = SharedPrefManager.getInstance(getContext()).getUser();
        uid = String.valueOf(user.getUSERID());

        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ratingBarBehaviour.getVisibility() == View.VISIBLE) {
                    ratingBarBehaviour.setVisibility(View.GONE);
                    ratingBarAccuracy.setVisibility(View.VISIBLE);
                    rateTitle.setText("Accuracy Of Operator");
                    bg.setBackgroundColor(Color.parseColor("#4287f5"));
                    rateDescription.setText("Have you liked Timing of Our Operator? Would you mind to rate us of Operator.");
                    comments.setVisibility(View.GONE);
                //    Log.e("ratingBarBehaviour", String.valueOf(userRateBehaviour));
                } else if (ratingBarAccuracy.getVisibility() == View.VISIBLE) {
                    ratingBarBehaviour.setVisibility(View.GONE);
                    ratingBarAccuracy.setVisibility(View.GONE);
                    comments.setVisibility(View.VISIBLE);
                    bg.setBackgroundColor(Color.parseColor("#42f5c2"));
                    rateTitle.setText("Comment About Operator");
                    rateDescription.setText("Tell us more about operator.");
                    ratingImage.setImageResource(R.drawable.ic_comments);
                 //   Log.e("ratingBarAccuracy", String.valueOf(userRateAccuracy));
                    rateNowBtn.setText("Rate Now");
                } else {
                    adddata();
                 //   Log.e("Edittext", comments.getEditText().getText().toString());
                }

                // you code goes here
            }
        });

        /*laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // hide rating dialog
                dismiss();
            }
        });*/

        ratingBarBehaviour.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating == 1) {
                    ratingImage.setImageResource(R.drawable.ic_one_star);
                } else if (rating == 2) {
                    ratingImage.setImageResource(R.drawable.ic_two_star);
                } else if (rating == 3) {
                    ratingImage.setImageResource(R.drawable.ic_three_star);
                } else if (rating == 4) {
                    ratingImage.setImageResource(R.drawable.ic_four_star);
                } else if (rating == 5) {
                    ratingImage.setImageResource(R.drawable.ic_five_star);
                }

                // animate emoji image
                animateImage(ratingImage);

                // selected rating by user
                userRateBehaviour = rating;
              //  Log.e("aaa", String.valueOf(userRateBehaviour));
            }
        });

        ratingBarAccuracy.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating == 1) {
                    ratingImage.setImageResource(R.drawable.ic_one_star);
                } else if (rating == 2) {
                    ratingImage.setImageResource(R.drawable.ic_two_star);
                } else if (rating == 3) {
                    ratingImage.setImageResource(R.drawable.ic_three_star);
                } else if (rating == 4) {
                    ratingImage.setImageResource(R.drawable.ic_four_star);
                } else if (rating == 5) {
                    ratingImage.setImageResource(R.drawable.ic_five_star);
                }

                // animate emoji image
                animateImage(ratingImage);

                // selected rating by user
                userRateAccuracy = rating;
               // Log.e("aaa", String.valueOf(userRateBehaviour));
            }
        });
    }

    private void animateImage(ImageView ratingImage) {

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }


    public void adddata() {

        float rateBehaviour = ratingBarBehaviour.getRating();
        float rateAccuracy = ratingBarAccuracy.getRating();
        String comment = comments.getEditText().getText().toString();

        class UserLogin extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if(!obj.getBoolean("error")) {

                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("USERID", uid);
                params.put("OP_ID", op_IDS);
                params.put("APPT_ID", appt_id);
                params.put("BEHAVIOUR", String.valueOf(rateBehaviour));
                params.put("ACCURACY", String.valueOf(rateAccuracy));
                params.put("COMMENTS", comment);

                Log.e("IDS", appt_id);
                Log.e("BEHAVIOUR", String.valueOf(rateBehaviour));
                Log.e("ACCURACY", String.valueOf(rateAccuracy));
                Log.e("COMMENTS", comment);
                Log.e("USERID", uid);

                return requestHandler.sendPostRequest(URLs.URL_USER_ADD_REVIEWS, params);

            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
