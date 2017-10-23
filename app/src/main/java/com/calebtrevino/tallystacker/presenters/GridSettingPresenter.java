package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
public interface GridSettingPresenter
        extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void changeGrid(Grid grid);

    void setKeepUpdates(boolean checked);

    void setGridName(String gridName);
}
