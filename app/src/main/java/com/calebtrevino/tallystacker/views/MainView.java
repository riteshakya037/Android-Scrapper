package com.calebtrevino.tallystacker.views;


import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

public interface MainView extends BaseContextView, BaseToolbarView{
    public void initializeDrawerLayout();

    public void closeDrawerLayout();

    public int getMainLayoutId();
}
