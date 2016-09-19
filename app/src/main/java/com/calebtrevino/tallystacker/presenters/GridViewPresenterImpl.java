package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;
import com.calebtrevino.tallystacker.presenters.mapper.GridViewMapper;
import com.calebtrevino.tallystacker.views.GridViewView;
import com.calebtrevino.tallystacker.views.adaptors.GridViewAdapter;

/**
 * @author Ritesh Shakya
 */
public class GridViewPresenterImpl implements GridViewPresenter, ChildGameEventListener {

    private static final String TAG = GridViewPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private static final String CURRENT_GRID = TAG + ":" + "current_grid";

    private final GridViewView mGridViewView;
    private final GridViewMapper mGridViewMapper;
    private Parcelable mPositionSavedState;
    private GridViewAdapter mGridViewAdapter;

    private DatabaseContract.DbHelper dbHelper;
    private Grid currentGrid;

    public GridViewPresenterImpl(GridViewView gridViewView, GridViewMapper gridViewMapper) {

        this.mGridViewView = gridViewView;
        this.mGridViewMapper = gridViewMapper;
    }

    @Override
    public void initializeViews() {
        mGridViewView.initializeToolbar();
        mGridViewView.initializeEmptyRelativeLayout();
        mGridViewView.initializeRecyclerLayoutManager(new StaggeredGridLayoutManager(15, StaggeredGridLayoutManager.HORIZONTAL));
        mGridViewView.initializeBasePageView();
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mGridViewView.getActivity());
        dbHelper.addChildGameEventListener(this);
    }

    @Override
    public void saveState(Bundle outState) {
        if (mGridViewMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mGridViewMapper.getPositionState());
            outState.putParcelable(CURRENT_GRID, currentGrid);
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        if (savedState.containsKey(POSITION_PARCELABLE_KEY)) {
            mPositionSavedState = savedState.getParcelable(POSITION_PARCELABLE_KEY);
            changeGrid((Grid) savedState.getParcelable(CURRENT_GRID));
            restorePosition();
            savedState.remove(POSITION_PARCELABLE_KEY);
        }
    }

    @Override
    public void restorePosition() {
        if (mPositionSavedState != null) {
            mGridViewMapper.setPositionState(mPositionSavedState);

            mPositionSavedState = null;
        }
    }

    @Override
    public void isEmpty(boolean isEmpty) {
        if (isEmpty) {
            mGridViewView.showEmptyRelativeLayout();
        } else {
            mGridViewView.hideEmptyRelativeLayout();
        }
    }

    @Override
    public void changeGrid(Grid grid) {
        currentGrid = grid;
        initializeDataFromPreferenceSource();
        mGridViewView.initializeRecyclerLayoutManager(new StaggeredGridLayoutManager(grid.getRowNo(), StaggeredGridLayoutManager.HORIZONTAL));
    }


    @Override
    public void releaseAllResources() {
        if (mGridViewAdapter != null) {
            mGridViewAdapter = null;
        }
        if (dbHelper != null) {
            dbHelper.removeChildGameEventListener(this);
        }
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        mGridViewAdapter = new GridViewAdapter(mGridViewView.getActivity(), currentGrid);
        mGridViewMapper.registerAdapter(mGridViewAdapter);
        mGridViewView.hideEmptyRelativeLayout();
        mGridViewAdapter.setNullListener(this);
    }

    @Override
    public void onChildAdded(Game game) {
//        if (mGridViewAdapter != null)
//            mGridViewAdapter.addGames(game);
    }

    @Override
    public void onChildChanged(Game game) {
//                mDashAdapter.changeGame((Game) game);
    }

    @Override
    public void onChildRemoved(Game game) {
        mGridViewAdapter.removeCard(game);
    }

}
