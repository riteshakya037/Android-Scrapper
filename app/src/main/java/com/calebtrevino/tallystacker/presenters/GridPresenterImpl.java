package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.views.GridFragmentMapper;
import com.calebtrevino.tallystacker.views.GridFragmentView;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;

/**
 * Created by fatal on 9/5/2016.
 */
public class GridPresenterImpl implements GridPresenter {


    public static final String TAG = GridPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private final GridFragmentView mGridFragmentView;
    private final GridFragmentMapper mGridFragmentMapper;
    private GridFragmentPagerAdapter mCatalogueAdapter;
    private Parcelable mPositionSavedState;

    public GridPresenterImpl(GridFragmentView gridFragmentView, GridFragmentMapper gridFragmentMapper) {
        this.mGridFragmentView = gridFragmentView;
        this.mGridFragmentMapper = gridFragmentMapper;
    }

    @Override
    public void initializeViews() {
        mGridFragmentView.initializeToolbar();
        mGridFragmentView.initializeBasePageView();
        mGridFragmentView.initializeEmptyRelativeLayout();
    }

    @Override
    public void saveState(Bundle outState) {
        if (mGridFragmentMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mGridFragmentMapper.getPositionState());
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
    public void releaseAllResources() {
        if (mCatalogueAdapter != null) {
//            mCatalogueAdapter.setCursor(null);
            mCatalogueAdapter = null;
        }
    }

    private void restorePosition() {
        if (mPositionSavedState != null) {
            mGridFragmentMapper.setPositionState(mPositionSavedState);
            mPositionSavedState = null;
        }
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        mCatalogueAdapter = new GridFragmentPagerAdapter(mGridFragmentView.getFragmentManager(), mGridFragmentView.getContext());
        mGridFragmentMapper.registerAdapter(mCatalogueAdapter);
    }

    @Override
    public void initializeTabLayoutFromAdaptor() {
        mGridFragmentMapper.registerTabs(mCatalogueAdapter);
    }
}
