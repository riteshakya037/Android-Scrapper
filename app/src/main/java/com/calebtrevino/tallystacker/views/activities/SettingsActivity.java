package com.calebtrevino.tallystacker.views.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.services.ScrapperService;
import com.calebtrevino.tallystacker.models.Preferences.MultiProcessPreference;

public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.pref_general);
        } catch (Exception ignored) {

        }
        setContentView(R.layout.activity_setting);
        setupActionBar();
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void setupActionBar() {
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        if (bar != null) {
            bar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (key.equals(getString(R.string.key_bid_update_time))) {
            Intent i = new Intent(getBaseContext(), ScrapperService.class);
            startService(i);
            saveToken(key);
            System.out.println("TEST" + MultiProcessPreference.getDefaultSharedPreferences(getBaseContext()).getString(key, null));
        }
    }

    public void saveToken(String key) {
        MultiProcessPreference.getDefaultSharedPreferences(getBaseContext()).edit().putString(key, PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(key, "")).commit();//or apply()
    }
}
