package com.calebtrevino.tallystacker.controllers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Ritesh Shakya
 */
public class GameUpdateReceiver extends BroadcastReceiver {


    public static final String ACTION_GET_RESULT = "get_result";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getLongExtra("game", 0L));
    }
}
