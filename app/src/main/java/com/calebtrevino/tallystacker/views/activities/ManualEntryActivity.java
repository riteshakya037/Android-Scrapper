package com.calebtrevino.tallystacker.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.InvalidScoreTypeException;
import com.calebtrevino.tallystacker.utils.ManualContext;
import com.calebtrevino.tallystacker.utils.Utils;
import com.calebtrevino.tallystacker.views.adaptors.ManualPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Ritesh Shakya
 */

public class ManualEntryActivity extends AppCompatActivity {

    private static final String TAG = ManualEntryActivity.class.getSimpleName();

    @BindView(R.id.container)
    ViewPager mViewPager;

    ManualPagerAdapter adapter;

    @OnClick(R.id.save_game)
    void saveGame() {
        try {
            adapter.getFragment(mViewPager.getCurrentItem()).saveGame();
            adapter.remove(mViewPager.getCurrentItem());
        } catch (InvalidScoreTypeException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.open_settings)
    void openSettings() {
        startActivity(new Intent(ManualEntryActivity.this, SettingsActivity.class));
        finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onReceive: Manual");

        ManualContext.getInstance(this).getManualGames();

        if (ManualContext.getInstance(this).getGameList().size() == 0) {
            openSettings();
        }
        setContentView(R.layout.activity_manual_entry);
        ButterKnife.bind(this);
        Utils utils = new Utils(this);

        adapter = new ManualPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);
        adapter.setData(ManualContext.getInstance(this).getGameList());
        getWindow().setLayout(utils.getScreenWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
    }
}