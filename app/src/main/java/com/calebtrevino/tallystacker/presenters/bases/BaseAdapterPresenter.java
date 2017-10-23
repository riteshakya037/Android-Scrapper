package com.calebtrevino.tallystacker.presenters.bases;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused") public interface BaseAdapterPresenter {
    void releaseAllResources();

    void restorePosition();

    void initializeDataFromPreferenceSource();
}
