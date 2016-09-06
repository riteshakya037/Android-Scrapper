package com.calebtrevino.tallystacker.presenters;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

public interface MainPresenter extends NavigationView.OnNavigationItemSelectedListener, BasePresenter {

    public void initializeMainLayout(Intent argument);

    @Override
    boolean onNavigationItemSelected(MenuItem item);
}
