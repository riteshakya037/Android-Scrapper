package com.calebtrevino.tallystacker.views.activities;

import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;

import static com.calebtrevino.tallystacker.controllers.receivers.ErrorReceiver.ERROR_BROADCAST;

/**
 * @author Ritesh Shakya
 */

public class TallyStackerApplication extends android.app.Application {
    private static final String TAG = TallyStackerApplication.class.getSimpleName();
    private static TallyStackerApplication instance;

    public static TallyStackerApplication get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TallyStackerApplication.instance = this;
        JodaTimeAndroid.init(this);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        Stetho.initializeWithDefaults(this);
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(e);
            }
        });
    }

    public void handleUncaughtException(Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Log.i(TAG, "handleUncaughtException: ");
        Intent gameIntent = new Intent(ERROR_BROADCAST);
        sendBroadcast(gameIntent);
    }
}
