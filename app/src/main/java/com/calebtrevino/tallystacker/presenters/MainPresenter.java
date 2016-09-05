package com.calebtrevino.tallystacker.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

public interface MainPresenter extends NavigationView.OnNavigationItemSelectedListener {
    public void initializeViews();

    public void initializeMainLayout(Intent argument);

    public void saveState(Bundle outState);

    public void restoreState(Bundle savedState);

    @Override
    boolean onNavigationItemSelected(MenuItem item);
}
