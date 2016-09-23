package com.calebtrevino.tallystacker.controllers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Ritesh Shakya
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(BootReceiver.class.getName(), "onReceive: BOOT");

        Toast.makeText(context, "Started", Toast.LENGTH_SHORT);
//        Intent i = new Intent(context, ScrapperService.class);
//        context.startService(i);
    }
}