package com.example.pk836_6senses.Operator;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pk836_6senses.Model.User;
import com.example.pk836_6senses.Model.op_rating_model;
import com.example.pk836_6senses.No_Internet.InternetReceiver;
import com.example.pk836_6senses.R;
import com.example.pk836_6senses.RequestHandler;
import com.example.pk836_6senses.SharedPrefManager;
import com.example.pk836_6senses.URLs;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowReviews extends MyBaseActivity_Operator {

    int behaviour1;
    int behaviour2;
    int behaviour3;
    int behaviour4;
    int behaviour5;
    int behaviourMax;
    int accuracy1, accuracy2, accuracy3, accuracy4, accuracy5, accuracyMax;
    String uid;
    User user;

    String BEHAVIOUR_1;
    String BEHAVIOUR_2;
    String BEHAVIOUR_3;
    String BEHAVIOUR_4;
    String BEHAVIOUR_5;
    String TOTAL_BEHAVIOUR;
    String ACCURACY_1;
    String ACCURACY_2;
    String ACCURACY_3;
    String ACCURACY_4;
    String ACCURACY_5;
    String fname, lname, comment, brating, arating, userid, apptid;
    String TOTAL_ACCURACY;
    RatingReviews ratingBehaviour;
    RatingReviews ratingAccuracy;
    List<op_rating_model> list;
    RecyclerView recyclerView;
    RecyclerAdapter radapter;

    //For No internet
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.updateResource(sharedPrefManager.getLang());

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_show_reviews, null, false);
        drawer.addView(v, 0);


        //For No Internet
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
        user = SharedPrefManager.getInstance(this).getUser();
        uid = user.getUSERID();
        Log.e("uid", uid);

        ratingBehaviour = findViewById(R.id.rating_reviews_behaviour);
        ratingAccuracy = findViewById(R.id.rating_reviews_accuracy);
        recyclerView = findViewById(R.id.reviewList);

        //new item to top
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
        LayoutManager.setReverseLayout(true);
        LayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(LayoutManager);

        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        radapter = new RecyclerAdapter(getAllData(), this);
        recyclerView.setAdapter(radapter);

        //ratingAccuracy.createRatingBars(100, BarLabels.STYPE1, colors, raters);
        getOpLocation();
    }


    //For No Internet
    public void InternetStatus() {
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception e) {
            // already registered
        }
    }

    private void getOpLocation() {

        class GetOpLocation extends AsyncTask<Void, Void, String> {

            //ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressBar = (ProgressBar) findViewById(R.id.progressBar_login_citizen);
                //progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONObject userJson = obj.getJSONObject("BEHAVIOUR");
                        JSONObject userJson1 = obj.getJSONObject("ACCURACY");

                        BEHAVIOUR_1 = userJson.getString("BEHAVIOUR_1");
                        BEHAVIOUR_2 = userJson.getString("BEHAVIOUR_2");
                        BEHAVIOUR_3 = userJson.getString("BEHAVIOUR_3");
                        BEHAVIOUR_4 = userJson.getString("BEHAVIOUR_4");
                        BEHAVIOUR_5 = userJson.getString("BEHAVIOUR_5");
                        TOTAL_BEHAVIOUR = userJson.getString("TOTAL_BEHAVIOUR");

                        ACCURACY_1 = userJson1.getString("ACCURACY_1");
                        ACCURACY_2 = userJson1.getString("ACCURACY_2");
                        ACCURACY_3 = userJson1.getString("ACCURACY_3");
                        ACCURACY_4 = userJson1.getString("ACCURACY_4");
                        ACCURACY_5 = userJson1.getString("ACCURACY_5");
                        TOTAL_ACCURACY = userJson1.getString("TOTAL_ACCURACY");

                        behaviour1 = Integer.parseInt(BEHAVIOUR_1);
                        behaviour2 = Integer.parseInt(BEHAVIOUR_2);
                        behaviour3 = Integer.parseInt(BEHAVIOUR_3);
                        behaviour4 = Integer.parseInt(BEHAVIOUR_4);
                        behaviour5 = Integer.parseInt(BEHAVIOUR_5);
                        behaviourMax = Integer.parseInt(TOTAL_BEHAVIOUR);

                        accuracy1 = Integer.parseInt(ACCURACY_1);
                        accuracy2 = Integer.parseInt(ACCURACY_2);
                        accuracy3 = Integer.parseInt(ACCURACY_3);
                        accuracy4 = Integer.parseInt(ACCURACY_4);
                        accuracy5 = Integer.parseInt(ACCURACY_5);
                        accuracyMax = Integer.parseInt(TOTAL_ACCURACY);

                        int[] colors = new int[]{
                                Color.parseColor("#0e9d58"),
                                Color.parseColor("#bfd047"),
                                Color.parseColor("#ffc105"),
                                Color.parseColor("#ef7e14"),
                                Color.parseColor("#d36259")};

                        Log.e("behaviour1", String.valueOf(behaviour1));


                        int[] ratersBehaviour = new int[]{
                                behaviour5,
                                behaviour4,
                                behaviour3,
                                behaviour2,
                                behaviour1
                        };
                        ratingBehaviour.createRatingBars(behaviourMax, BarLabels.STYPE1, colors, ratersBehaviour);

                        int[] ratersAccuracy = new int[]{
                                accuracy5,
                                accuracy4,
                                accuracy3,
                                accuracy2,
                                accuracy1
                        };
                        ratingAccuracy.createRatingBars(accuracyMax, BarLabels.STYPE1, colors, ratersAccuracy);


                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("OP_ID", uid);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_GET_BEHAVIOUR_RATING, params);
            }
        }

        GetOpLocation ul = new GetOpLocation();
        ul.execute();
    }

    private List<op_rating_model> getAllData() {

        final List<op_rating_model> data = new ArrayList<>();

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
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        // JSONObject userJson = obj.getJSONObject("vehicle");

                        JSONArray jsonArray = obj.getJSONArray("COMMENT");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            fname = jsonObject1.getString("F_NAME");
                            lname = jsonObject1.getString("L_NAME");
                            comment = jsonObject1.getString("COMMENTS");
                            brating = jsonObject1.getString("BEHAVIOUR");
                            arating = jsonObject1.getString("ACCURACY");
                            userid = jsonObject1.getString("APPT_ID");
                            apptid = jsonObject1.getString("USERID");
                            Log.e("fname", fname);

                            op_rating_model current1 = new op_rating_model();
                            current1.F_NAME = fname;
                            current1.L_NAME = lname;
                            current1.COMMENTS = comment;
                            current1.BEHAVIOUR = brating;
                            current1.ACCURACY = arating;
                            current1.USERID = userid;
                            current1.APPT_ID = apptid;
                            data.add(current1);
                        }

                        radapter.notifyDataSetChanged();

                    } else {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
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
                params.put("OP_ID", uid);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_OP_GET_BEHAVIOUR_RATING, params);

                //returing the response


            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
        return data;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder> {

        List<op_rating_model> list;
        Context context;

        public RecyclerAdapter(List<op_rating_model> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_citizen_review_history, parent, false);
            return new RecyclerAdapter.MyviewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.MyviewHolder holder, @SuppressLint("RecyclerView") final int position) {

            op_rating_model current1 = list.get(position);


            holder.op_name.setText(current1.getF_NAME() + " " + current1.getL_NAME());
            holder.ratingsBehavior.setText("BEHAVIOUR ⭐ " + current1.getBEHAVIOUR());
            holder.ratingsAccuracy.setText("ACCURACY ⭐ " + current1.getACCURACY());
            holder.description.setText(current1.getCOMMENTS());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {


            private final TextView op_name;
            private final TextView ratingsBehavior;
            private final TextView ratingsAccuracy;
            private final TextView description;

            public MyviewHolder(@NonNull View itemView) {
                super(itemView);

                op_name = itemView.findViewById(R.id.op_name);
                ratingsBehavior = itemView.findViewById(R.id.ratingsBehavior);
                ratingsAccuracy = itemView.findViewById(R.id.ratingsAccuracy);
                description = itemView.findViewById(R.id.description);


            }
        }
    }

}