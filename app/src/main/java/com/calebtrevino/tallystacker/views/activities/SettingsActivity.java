package com.calebtrevino.tallystacker.views.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.services.ScrapperService;
import com.calebtrevino.tallystacker.models.database.DatabaseDump;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;

import static com.calebtrevino.tallystacker.controllers.services.ScrapperService.FETCH_TIME_CHANGE;

@SuppressWarnings("deprecation")
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    @SuppressWarnings("unused")
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            addPreferencesFromResource(R.xml.pref_general);
        } catch (Exception ignored) {

        }
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.activity_settings_menu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            onBackPressed();
            return true;
        } else if (id == R.id.action_upload) {
            DatabaseDump.getInstance(this).exportData();
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_bid_update_time))) {
            Intent i = new Intent(getBaseContext(), ScrapperService.class);
            i.putExtra(FETCH_TIME_CHANGE, key);
            startService(i);
            saveStringToken(key);
        } else if (key.equals(getString(R.string.key_notification_show))) {
            saveBooleanToken(key);
        } else if (key.equals(getString(R.string.key_notification_ringtone))) {
            saveStringToken(key);
        } else if (key.equals(getString(R.string.key_retry_frequency))) {
            saveIntToken(key);
        }
    }

    private void saveStringToken(String key) {
        MultiProcessPreference.getDefaultSharedPreferences(getBaseContext()).edit().putString(key, PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(key, "")).commit();//or apply()
    }

    private void saveIntToken(String key) {
        MultiProcessPreference.getDefaultSharedPreferences(getBaseContext()).edit().putString(key, PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(key, "15")).commit();//or apply()
    }

    private void saveBooleanToken(String key) {
        MultiProcessPreference.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(key, PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(key, true)).commit();//or apply()
    }
}
