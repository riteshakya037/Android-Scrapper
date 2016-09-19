package com.calebtrevino.tallystacker.presenters.bases;

import com.calebtrevino.tallystacker.models.listeners.GridChangeListener;

/**
  * @author Ritesh Shakya
  */
public interface BaseGridChangePresenter {
    void setGridChangeListener(GridChangeListener gridChangeListener);
}
