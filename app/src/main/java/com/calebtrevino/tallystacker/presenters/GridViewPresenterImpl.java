package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.base.BaseModel;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;
import com.calebtrevino.tallystacker.presenters.mapper.GridViewMapper;
import com.calebtrevino.tallystacker.views.GridViewView;
import com.calebtrevino.tallystacker.views.adaptors.GridViewAdapter;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridViewPresenterImpl implements GridViewPresenter, ChildGameEventListener {

    public static final String TAG = GridViewPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private final GridViewView mGridViewView;
    private final GridViewMapper mGridViewMapper;
    private Parcelable mPositionSavedState;
    private GridViewAdapter mGridViewAdapter;

    DatabaseContract.DbHelper dbHelper;

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
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        if (savedState.containsKey(POSITION_PARCELABLE_KEY)) {
            mPositionSavedState = savedState.getParcelable(POSITION_PARCELABLE_KEY);

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
        mGridViewAdapter = new GridViewAdapter(mGridViewView.getActivity());
        mGridViewMapper.registerAdapter(mGridViewAdapter);
        mGridViewAdapter.setNullListener(this);
        dbHelper.selectRecentGames(15);
    }

    @Override
    public void onChildAdded(BaseModel baseModel) {
        mGridViewAdapter.addGame((Game) baseModel);
    }

    @Override
    public void onChildChanged(BaseModel baseModel) {
//                mDashAdapter.changeame((Game) baseModel);
    }

    @Override
    public void onChildRemoved(BaseModel baseModel) {
        mGridViewAdapter.removeCard((Game) baseModel);
    }

}
