package com.calebtrevino.tallystacker.presenters.bases;

import android.os.Bundle;

/**
 * Created by fatal on 9/6/2016.
 */
public interface BasePresenter {

    public void initializeViews();

    public void saveState(Bundle outState);

    public void restoreState(Bundle savedState);
}
