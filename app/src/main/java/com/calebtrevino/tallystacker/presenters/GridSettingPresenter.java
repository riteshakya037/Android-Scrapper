package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BaseDatabasePresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * Created by fatal on 9/16/2016.
 */
public interface GridSettingPresenter extends BasePresenter, BaseAdapterPresenter, BaseDatabasePresenter {
    void changeGrid(Grid grid);

    void setForceSwitch(boolean checked);

    void setKeepUpdates(boolean checked);

    void setGridName(String gridName);
}
