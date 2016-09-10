package com.calebtrevino.tallystacker.presenters;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.listeners.FinishedListener;
import com.calebtrevino.tallystacker.models.listeners.GridChangeListener;
import com.calebtrevino.tallystacker.presenters.mapper.GridPagerMapper;
import com.calebtrevino.tallystacker.views.GridPagerView;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;
import com.calebtrevino.tallystacker.views.custom.CreateNewGridDialog;

/**
 * Created by fatal on 9/5/2016.
 */
public class GridPagePresenterImpl implements GridPagePresenter {


    public static final String TAG = GridPagePresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private final GridPagerView mGridPagerView;
    private final GridPagerMapper mGridPagerMapper;
    private GridFragmentPagerAdapter mGridPageAdapter;
    private Parcelable mPositionSavedState;
    private DatabaseContract.DbHelper dbHelper;
    private GridChangeListener gridChangeListener;

    public GridPagePresenterImpl(GridPagerView gridPagerView, GridPagerMapper gridPagerMapper) {
        this.mGridPagerView = gridPagerView;
        this.mGridPagerMapper = gridPagerMapper;
    }

    @Override
    public void initializeViews() {
        mGridPagerView.initializeToolbar();
        mGridPagerView.initializeBasePageView();
        mGridPagerView.initializeEmptyRelativeLayout();
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mGridPagerView.getActivity());
    }

    @Override
    public void saveState(Bundle outState) {
        if (mGridPagerMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mGridPagerMapper.getPositionState());
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
        if (mGridPageAdapter != null) {
//            mGridPageAdapter.setCursor(null);
            mGridPageAdapter = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void restorePosition() {
        if (mPositionSavedState != null) {
            mGridPagerMapper.setPositionState(mPositionSavedState);
            mPositionSavedState = null;
        }
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        if (dbHelper.getGrids().isEmpty()) {
            mGridPagerView.showEmptyRelativeLayout();
        } else {
            mGridPagerView.hideEmptyRelativeLayout();
            mGridPageAdapter = new GridFragmentPagerAdapter(mGridPagerView.getFragmentManager(), mGridPagerView.getActivity());
            mGridPagerMapper.registerAdapter(mGridPageAdapter);
            initializeTabLayoutFromAdaptor();
        }
    }

    @Override
    public void initializeTabLayoutFromAdaptor() {
        mGridPagerMapper.registerTabs(mGridPageAdapter);
    }

    @Override
    public void createNewGrid() {
        final CreateNewGridDialog createNew = new CreateNewGridDialog(mGridPagerView.getActivity());
        createNew.show();
        createNew.setFinishedListener(new FinishedListener() {
            @Override
            public void onFinished(Grid grid, ProgressDialog progressDialog) {
                dbHelper.onInsertGrid(grid);
                initializeDataFromPreferenceSource();
                mGridPageAdapter.added(grid);
                progressDialog.dismiss();
                createNew.dismiss();
            }
        });
    }
}
