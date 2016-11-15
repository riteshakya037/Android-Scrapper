package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
public interface GridCalendarPresenter extends BasePresenter, BaseAdapterPresenter {
    void nextMonth();

    void previousMonth();

    void initializeData();
}
