package com.calebtrevino.tallystacker.presenters.bases;

import com.calebtrevino.tallystacker.models.listeners.GridChangeListener;

/**
 * Created by fatal on 9/9/2016.
 */
public interface BaseGridChangePresenter {
    void setGridChangeListener(GridChangeListener gridChangeListener);
}
