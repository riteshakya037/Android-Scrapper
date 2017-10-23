package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */

public interface DashPagerPresenter
        extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void isEmpty(boolean isEmpty);
}
