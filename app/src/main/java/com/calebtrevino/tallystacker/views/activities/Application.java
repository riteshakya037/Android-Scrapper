package com.calebtrevino.tallystacker.views.activities;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;

/**
 * @author Ritesh Shakya
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
    }
}
