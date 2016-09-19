package com.calebtrevino.tallystacker.presenters.bases;

/**
 * @author Ritesh Shakya
 */
public interface BaseAdapterPresenter {
    void releaseAllResources();

    void restorePosition();

    void initializeDataFromPreferenceSource();
}
