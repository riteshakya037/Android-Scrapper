package com.calebtrevino.tallystacker.controllers.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.calebtrevino.tallystacker.controllers.services.ScrapperService;

/**
 * @author Ritesh Shakya
 */

public class BootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(BootReceiver.class.getName(), "onReceive: BOOT");

        Toast.makeText(context, "Started", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, ScrapperService.class);
        context.startService(i);
    }
}