package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * Created by Ritesh on 0012, May 12, 2017.
 */

public interface DashPagerPresenter extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void isEmpty(boolean isEmpty);
}
