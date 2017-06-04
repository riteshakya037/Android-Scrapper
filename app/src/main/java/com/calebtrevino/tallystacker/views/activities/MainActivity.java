package com.calebtrevino.tallystacker.views.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.services.ScrapperService;
import com.calebtrevino.tallystacker.presenters.MainPresenter;
import com.calebtrevino.tallystacker.presenters.MainPresenterImpl;
import com.calebtrevino.tallystacker.views.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String POSITION_ARGUMENT_KEY = TAG + ":" + "PositionArgumentKey";
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
    private MainPresenter mMainPresenter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainPresenter = new MainPresenterImpl(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMainPresenter.initializeViews();
        if (savedInstanceState != null) {
            mMainPresenter.restoreState(savedInstanceState);
        }
        Intent i = new Intent(getBaseContext(), ScrapperService.class);
        startService(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(null, null, ContextCompat.getColor(this, R.color.colorAccent));

            setTaskDescription(td);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spinner_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        mSpinner = (Spinner) MenuItemCompat.getActionView(item);
        mSpinner.setBackgroundResource(android.R.color.transparent);
        mMainPresenter.initializeMainLayout(getIntent());
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mMainPresenter.saveState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(navigationView)) {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initializeToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void initializeDrawerLayout() {
        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
            navigationView.setNavigationItemSelectedListener(mMainPresenter);
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }

    @Override
    public void closeDrawerLayout() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(navigationView);
        }
    }


    @Override
    public int getMainLayoutId() {
        return R.id.content_main2;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void isSpinnerVisible(boolean isVisible) {
        if (mSpinner != null)
            mSpinner.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }


}
