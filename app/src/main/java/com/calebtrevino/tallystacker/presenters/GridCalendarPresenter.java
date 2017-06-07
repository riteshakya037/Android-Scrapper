package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
public interface GridCalendarPresenter extends BasePresenter, BaseAdapterPresenter {
    void nextMonth();

    void previousMonth();

    void initializeData();

    void changeGrid(Grid grid);
}
