package com.calebtrevino.tallystacker.views.fragments;

/**
 * @author Ritesh Shakya
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