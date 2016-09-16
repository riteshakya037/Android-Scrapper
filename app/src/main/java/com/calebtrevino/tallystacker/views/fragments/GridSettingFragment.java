package com.calebtrevino.tallystacker.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridSettingFragment extends GridHolderFragment {

    private Grid mCurrentGrid;

    public static GridHolderFragment newInstance() {
        return new GridSettingFragment();
    }

    @OnClick(R.id.editName)
    void editName() {
        mGridName.setEnabled(true);
        TranslateAnimation animate = new TranslateAnimation(0, mEditButton.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        mEditButton.startAnimation(animate);
        mEditButton.setVisibility(View.GONE);
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                mGridName.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        mGridName.requestFocus();
        mGridName.moveCursorToVisibleOffset();
        mGridName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mCurrentGrid.setGridName(mGridName.getText().toString());
                    new DatabaseTask(new DatabaseContract.DbHelper(getActivity())) {
                        @Override
                        protected void callInUI(Object o) {

                        }

                        @Override
                        protected Object executeStatement(DatabaseContract.DbHelper dbHelper) {
                            dbHelper.onUpdateGrid(mCurrentGrid.get_id(), mCurrentGrid);
                            return null;
                        }
                    }.execute();
                    mGridName.setEnabled(false);
                    TranslateAnimation animate = new TranslateAnimation(mEditButton.getWidth(), 0, 0, 0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    mEditButton.startAnimation(animate);
                    mEditButton.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }

    @BindView(R.id.gridName)
    TextInputEditText mGridName;

    @BindView(R.id.rowNo)
    TextView mRowNo;

    @BindView(R.id.columnNo)
    TextView mColumnNo;

    @BindView(R.id.lastUpdated)
    TextView mLastUpdated;

    @BindView(R.id.editName)
    ImageButton mEditButton;

    @BindView(R.id.updateSwitch)
    Switch mUpdateSwitch;

    @BindView(R.id.forceSwitch)
    Switch mForceSwitch;

    @OnCheckedChanged(R.id.updateSwitch)
    void setUpdateSwitch() {
        mCurrentGrid.setKeepUpdates(mUpdateSwitch.isChecked());
        new DatabaseTask(new DatabaseContract.DbHelper(getActivity())) {
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

    @OnCheckedChanged(R.id.forceSwitch)
    void setForceSwitch() {
        mCurrentGrid.setForceAdd(mForceSwitch.isChecked());
        new DatabaseTask(new DatabaseContract.DbHelper(getActivity())) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_setting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void added(Grid grid) {
        mCurrentGrid = grid;
        mGridName.setText(grid.getGridName());
        mRowNo.setText(String.valueOf(grid.getRowNo()));
        mColumnNo.setText(String.valueOf(grid.getColumnNo()));
        mLastUpdated.setText(new SimpleDateFormat("EEE MMM dd", Locale.getDefault()).format(
                new Date(grid.getUpdatedOn())));
        mUpdateSwitch.setChecked(grid.isKeepUpdates());
        mForceSwitch.setChecked(grid.isForceAdd());
    }

}
