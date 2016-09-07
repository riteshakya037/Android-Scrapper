package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.presenters.mapper.GridViewMapper;
import com.calebtrevino.tallystacker.views.GridViewView;
import com.calebtrevino.tallystacker.views.adaptors.GridViewAdapter;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridViewPresenterImpl implements GridViewPresenter {

    public static final String TAG = GridViewPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private final GridViewView mGridViewView;
    private final GridViewMapper mGridViewMapper;
    private Parcelable mPositionSavedState;
    private GridViewAdapter mGridViewAdapter;


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
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        mGridViewAdapter = new GridViewAdapter(mGridViewView.getContext(), DefaultFactory.Grid.constructDefault());
        mGridViewMapper.registerAdapter(mGridViewAdapter);
        mGridViewAdapter.setNullListener(this);
    }
}
