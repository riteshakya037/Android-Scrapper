package com.calebtrevino.tallystacker.views.activities;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author Ritesh Shakya
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

    }
}
