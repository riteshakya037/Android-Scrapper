package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
@SuppressWarnings("unused")
public interface DashPresenter extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void isEmpty(boolean isEmpty);

    void initializeSpinner();

    void spinnerClicked(int position);
}
