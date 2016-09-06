package com.calebtrevino.tallystacker.presenters.bases;

/**
 * Created by fatal on 9/6/2016.
 */
public interface BaseAdapterPresenter {
    public void releaseAllResources();

    public void restorePosition();

    public void initializeDataFromPreferenceSource();
}
