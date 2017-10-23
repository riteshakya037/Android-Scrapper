package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
public interface DialogPresenter
        extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void createLeague();

    Grid getGrid();
}
