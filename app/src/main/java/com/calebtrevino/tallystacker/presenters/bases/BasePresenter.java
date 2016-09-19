package com.calebtrevino.tallystacker.presenters.bases;

import android.os.Bundle;

/**
 * @author Ritesh Shakya
 */
public interface BasePresenter {

    void initializeViews();

    void saveState(Bundle outState);

    void restoreState(Bundle savedState);
}
