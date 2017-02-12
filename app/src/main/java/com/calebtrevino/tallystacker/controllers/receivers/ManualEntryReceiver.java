package com.calebtrevino.tallystacker.controllers.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.views.activities.SettingsActivity;

import java.util.List;

/**
 * @author Ritesh Shakya
 */

public class ManualEntryReceiver extends BroadcastReceiver {
    public static final String ACTION_MANUAL = "ACTION_MANUAL";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(ManualEntryReceiver.class.getName(), "onReceive: Manual");
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(context);
        List<Game> gameList = dbHelper.getManualGames();
        if (gameList.size() == 0) {
            context.startActivity(new Intent(context, SettingsActivity.class));
        } else {
            System.out.println(gameList);
            //show dialog
        }
    }
}