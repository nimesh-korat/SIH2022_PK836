package com.example.pk836_6senses.No_Internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkInfo(context);
        if (status.equals("connected")){
           // Toast.makeText(context.getApplicationContext(), "Network Connected", Toast.LENGTH_SHORT).show();

        } else if(status.equals("disconnected")){
            Toast.makeText(context.getApplicationContext(), "No Internet Available!!!", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context.getApplicationContext(), NoInternet.class);
            context.startActivity(intent1);
        }
    }
}
