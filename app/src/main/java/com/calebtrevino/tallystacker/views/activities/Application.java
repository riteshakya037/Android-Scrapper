package com.calebtrevino.tallystacker.views.activities;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by fatal on 9/15/2016.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

    }
}
