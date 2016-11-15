package com.calebtrevino.tallystacker.views;


import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

@SuppressWarnings("SameReturnValue")
public interface MainView extends BaseContextView, BaseToolbarView, BaseSpinnerView {
    void initializeDrawerLayout();

    void closeDrawerLayout();

    int getMainLayoutId();
}
