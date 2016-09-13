package com.calebtrevino.tallystacker.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
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
    private SharedPreferences mPrefs;
    final String PREFS_NAME = "GridPagePrefs";
    private static final String VAL_CURRENT_GRID = "current_grid";

    public GridPagePresenterImpl(GridPagerView gridPagerView, GridPagerMapper gridPagerMapper) {
        this.mGridPagerView = gridPagerView;
        this.mGridPagerMapper = gridPagerMapper;
    }

    @Override
    public void initializePrefs() {
        mPrefs = mGridPagerView.getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
            final String currentGridId = mPrefs.getString(VAL_CURRENT_GRID, "0");
            if (!"0".equals(currentGridId)) {
                new DatabaseTask(dbHelper) {
                    @Override
                    protected Grid executeStatement(DatabaseContract.DbHelper dbHelper) {
                        return dbHelper.onSelectGrid(currentGridId);
                    }

                    @Override
                    protected void callInUI(Object o) {
                        mGridPageAdapter.changeTo((Grid) o);
                    }
                }.execute();
            }
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
                mPrefs.edit().putString(VAL_CURRENT_GRID, String.valueOf(grid.get_id())).apply(); // Set current set in preference.
                dbHelper.onInsertGrid(grid);
                initializeDataFromPreferenceSource();
//                mGridPageAdapter.changeTo(grid);
                progressDialog.dismiss();
                createNew.dismiss();
            }
        });
    }
}
