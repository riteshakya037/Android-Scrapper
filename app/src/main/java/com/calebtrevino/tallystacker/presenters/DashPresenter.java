package com.calebtrevino.tallystacker.presenters;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused") public interface DashPresenter {
    void initializeViews();

    void initializeSpinner();

    void spinnerClicked(int position);
}
