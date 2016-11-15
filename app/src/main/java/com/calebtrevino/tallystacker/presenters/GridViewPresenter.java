package com.calebtrevino.tallystacker.presenters;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterPresenter;
import com.calebtrevino.tallystacker.presenters.bases.BasePresenter;

/**
 * @author Ritesh Shakya
 */
public interface GridViewPresenter extends BasePresenter, BaseAdapterPresenter {
    void isEmpty(boolean isEmpty);

    void changeGrid(Grid grid);
}
