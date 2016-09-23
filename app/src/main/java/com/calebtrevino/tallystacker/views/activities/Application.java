package com.calebtrevino.tallystacker.views.activities;

import android.content.Intent;

import com.calebtrevino.tallystacker.controllers.services.ScrapperService;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * @author Ritesh Shakya
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        Intent i = new Intent(getBaseContext(), ScrapperService.class);
        startService(i);
    }
}
