package com.calebtrevino.tallystacker.views;


import android.support.v4.widget.DrawerLayout;

import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

public interface MainView extends BaseContextView, BaseToolbarView {
    public void initializeDrawerLayout();

    public void closeDrawerLayout();

    public int getNavigationLayoutId();

    public DrawerLayout getDrawerLayout();

    public int getMainLayoutId();
}
