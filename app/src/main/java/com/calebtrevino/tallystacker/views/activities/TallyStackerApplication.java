package com.calebtrevino.tallystacker.views.activities;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.stetho.Stetho;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;

/**
 * @author Ritesh Shakya
 */

public class TallyStackerApplication extends android.app.Application {
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
    }
}
