package com.calebtrevino.tallystacker.views.fragments;

/**
 * Created by fatal on 9/6/2016.
 */


import com.calebtrevino.tallystacker.models.listeners.GridChangeListener;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class GridHolderFragment extends BaseViewPagerFragment implements GridChangeListener {

    public GridChangeListener getGridChangeListener() {
        return this;
    }
}