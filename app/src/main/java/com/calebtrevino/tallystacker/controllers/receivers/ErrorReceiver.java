package com.calebtrevino.tallystacker.controllers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.calebtrevino.tallystacker.controllers.events.ErrorEvent;
import com.calebtrevino.tallystacker.utils.LogWriter;
import org.greenrobot.eventbus.EventBus;

/**
 * @author Ritesh Shakya
 */

public class ErrorReceiver extends BroadcastReceiver {
    public static final String ERROR_BROADCAST =
            "com.calebtrevino.tallystacker.android.action.broadcast";

    @Override public void onReceive(Context context, Intent intent) {
        LogWriter.writeLog(context);
        EventBus.getDefault().post(new ErrorEvent(true));
    }
}
