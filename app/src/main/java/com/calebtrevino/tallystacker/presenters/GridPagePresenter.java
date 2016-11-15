package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePreferencePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused")
public interface GridPagePresenter extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter, BasePreferencePresenter {

    void initializeTabLayoutFromAdaptor();

    void createNewGrid();

    void initializeSpinner();

    void spinnerClicked(int position);
}
