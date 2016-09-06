package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;

/**
 * Created by fatal on 9/5/2016.
 */
public interface GridPresenter {
    public void initializeViews();

    public void saveState(Bundle outState);

    public void restoreState(Bundle savedState);

    public void releaseAllResources();

    public void initializeDataFromPreferenceSource();

    void initializeTabLayoutFromAdaptor();
}
