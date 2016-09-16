package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;

import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
import com.calebtrevino.tallystacker.presenters.mapper.GridSettingMapper;
import com.calebtrevino.tallystacker.views.GridSettingView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fatal on 9/16/2016.
 */
public class GridSettingPresenterImpl implements GridSettingPresenter {
    public static final String TAG = GridSettingPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private static final String CURRENT_GRID = TAG + ":" + "current_grid";
    private GridSettingView mGridSettingView;
    private GridSettingMapper mGridSettingMapper;
    private Grid mCurrentGrid;
    private DatabaseContract.DbHelper dbHelper;

    public GridSettingPresenterImpl(GridSettingView gridSettingView, GridSettingMapper gridSettingMapper) {
        mGridSettingView = gridSettingView;
        mGridSettingMapper = gridSettingMapper;
    }

    @Override
    public void initializeViews() {
        mGridSettingView.initializeToolbar();
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mGridSettingView.getActivity());
    }

    @Override
    public void saveState(Bundle outState) {
        outState.putParcelable(CURRENT_GRID, mCurrentGrid);
    }

    @Override
    public void restoreState(Bundle savedState) {
        changeGrid((Grid) savedState.getParcelable(CURRENT_GRID));
        savedState.remove(POSITION_PARCELABLE_KEY);
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        if (mCurrentGrid != null) {
            mGridSettingMapper.setGridName(mCurrentGrid.getGridName());
            mGridSettingMapper.setRowCount(String.valueOf(mCurrentGrid.getRowNo()));
            mGridSettingMapper.setColumnCount(String.valueOf(mCurrentGrid.getColumnNo()));
            mGridSettingMapper.setLastUpdatedDate(new SimpleDateFormat("EEE MMM dd", Locale.getDefault()).format(
                    new Date(mCurrentGrid.getUpdatedOn())));
            mGridSettingMapper.setKeepUpdates(mCurrentGrid.isKeepUpdates());
            mGridSettingMapper.setForceAdd(mCurrentGrid.isForceAdd());
            writeToDatabase();
        }
    }

    private void writeToDatabase() {
        new DatabaseTask(dbHelper) {
            @Override
            protected void callInUI(Object o) {

            }

            @Override
            protected Object executeStatement(DatabaseContract.DbHelper dbHelper) {
                dbHelper.onUpdateGrid(mCurrentGrid.get_id(), mCurrentGrid);
                return null;
            }
        }.execute();
    }

    @Override
    public void releaseAllResources() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void restorePosition() {

    }


    @Override
    public void changeGrid(Grid grid) {
        mCurrentGrid = grid;
        initializeDataFromPreferenceSource();
    }

    @Override
    public void setForceSwitch(boolean checked) {
        mCurrentGrid.setForceAdd(checked);
        initializeDataFromPreferenceSource();
    }

    @Override
    public void setKeepUpdates(boolean checked) {
        mCurrentGrid.setKeepUpdates(checked);
    }

    @Override
    public void setGridName(String gridName) {
        mCurrentGrid.setGridName(gridName);
        initializeDataFromPreferenceSource();
    }
}
