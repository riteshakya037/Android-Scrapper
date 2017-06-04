package com.calebtrevino.tallystacker.presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
import com.calebtrevino.tallystacker.models.listeners.FinishedListener;
import com.calebtrevino.tallystacker.presenters.mapper.GridPagerMapper;
import com.calebtrevino.tallystacker.views.GridPagerView;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;
import com.calebtrevino.tallystacker.views.custom.CreateNewGridDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ritesh Shakya
 */
public class GridPagePresenterImpl implements GridPagePresenter, GridNameChangeListener {


    private static final String TAG = GridPagePresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private static final String VAL_CURRENT_GRID = "current_grid";
    private final GridPagerView mGridPagerView;
    private final GridPagerMapper mGridPagerMapper;
    @SuppressWarnings("FieldCanBeLocal")
    private final String PREFS_NAME = "GridPagePrefs";
    private GridFragmentPagerAdapter mGridPageAdapter;
    private Parcelable mPositionSavedState;
    private DatabaseContract.DbHelper dbHelper;
    private SharedPreferences mPrefs;
    private HashMap<String, String> grids;
    private ArrayAdapter<String> mSpinnerAdapter;
    private List<Game> gameList;

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
        mGridPagerMapper.initializeSpinnerListener();
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
        final String currentGridId = mPrefs.getString(VAL_CURRENT_GRID, "0");
        if ("0".equals(currentGridId))
            mGridPagerView.showEmptyRelativeLayout();
        else {
            mGridPagerView.showLoadingRelativeLayout();
        }
        new DatabaseTask<Grid>(dbHelper) {
            @Override
            protected void callInUI(Grid grid) {
                if (grid != null) {
                    updateSpinner();
                    mGridPageAdapter = new GridFragmentPagerAdapter(mGridPagerView.getFragmentManager(), mGridPagerView.getActivity(), GridPagePresenterImpl.this);
                    mGridPagerView.hideEmptyRelativeLayout();
                    mGridPagerMapper.registerAdapter(mGridPageAdapter);
                    initializeTabLayoutFromAdaptor();
                    mGridPageAdapter.changeTo(grid);
                    mGridPagerView.setCurrentSpinner(mSpinnerAdapter.getPosition(String.valueOf(grid.getGridName())));
                }
                mGridPagerView.fabVisibility(true);
            }

            @Override
            protected Grid executeStatement(DatabaseContract.DbHelper dbHelper) {
                grids = dbHelper.getGridKeys();
                if (gameList == null) {
                    mGridPagerView.fabVisibility(false);
                    gameList = dbHelper.selectUpcomingGames();
                }
                if (!grids.isEmpty() && !"0".equals(currentGridId)) {
                    return dbHelper.onSelectGrid(currentGridId);
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void initializeTabLayoutFromAdaptor() {
        mGridPagerMapper.registerTabs(mGridPageAdapter);
    }

    @Override
    public void createNewGrid() {
        final CreateNewGridDialog createNew = new CreateNewGridDialog(mGridPagerView.getActivity(), gameList);
        createNew.show();
        createNew.setFinishedListener(new FinishedListener() {
            @Override
            public void onFinished(Grid grid) {
                createNew.dismiss();
                ProgressDialog dialog = new ProgressDialog(mGridPagerView.getActivity());
                dialog.setMessage("Creating Grid");
                dialog.show();
                mPrefs.edit().putString(VAL_CURRENT_GRID, String.valueOf(grid.getId())).apply(); // Set current set in preference.
                dbHelper.addGamesToGrid(grid);
                initializeDataFromPreferenceSource();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void initializeSpinner() {
        mSpinnerAdapter = new ArrayAdapter<>(mGridPagerView.getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>());
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGridPagerMapper.registerSpinner(mSpinnerAdapter);
    }

    @Override
    public void spinnerClicked(int position) {
        for (Map.Entry<String, String> gridSet : grids.entrySet()) {
            if (gridSet.getValue().equals(mSpinnerAdapter.getItem(position))) {
                mPrefs.edit().putString(VAL_CURRENT_GRID, String.valueOf(gridSet.getKey())).apply();
                initializeDataFromPreferenceSource();
            }
        }
    }

    private void updateSpinner() {
        mSpinnerAdapter.clear();
        mSpinnerAdapter.addAll(grids.values());
        mSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void nameChanged(String gridId, String gridName) {
        grids.put(gridId, gridName);
        updateSpinner();
    }
}
