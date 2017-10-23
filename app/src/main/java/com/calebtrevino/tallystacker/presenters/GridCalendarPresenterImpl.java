package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.mapper.GridCalendarMapper;
import com.calebtrevino.tallystacker.views.GridCalendarView;
import com.calebtrevino.tallystacker.views.adaptors.GridCalendarAdapter;
import java.util.Calendar;

/**
 * @author Ritesh Shakya
 */
public class GridCalendarPresenterImpl implements GridCalendarPresenter {
    private static final String TAG = GridCalendarPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private static final String CURRENT_MONTH = TAG + ":" + "CurrentMonth";
    private static final String CURRENT_YEAR = TAG + ":" + "CurrentYear";
    private final GridCalendarView mGridCalendarView;
    private final GridCalendarMapper mGridCalendarMapper;
    private Parcelable mPositionSavedState;
    private GridCalendarAdapter mGridCalendarAdapter;
    private int mMonth;
    private int mYear;
    private Grid currentGrid;

    public GridCalendarPresenterImpl(GridCalendarView gridCalendarView,
            GridCalendarMapper gridCalendarMapper) {

        this.mGridCalendarView = gridCalendarView;
        this.mGridCalendarMapper = gridCalendarMapper;
    }

    private static String theMonth(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"
        };
        return monthNames[month];
    }

    @Override public void initializeData() {
        Calendar mCalendar = Calendar.getInstance();
        mMonth = mCalendar.get(Calendar.MONTH);
        mYear = mCalendar.get(Calendar.YEAR);
    }

    @Override public void initializeDataFromPreferenceSource() {
        mGridCalendarAdapter =
                new GridCalendarAdapter(mGridCalendarView.getActivity(), mMonth, mYear,
                        currentGrid);
        mGridCalendarMapper.registerAdapter(mGridCalendarAdapter);
        mGridCalendarMapper.setMonthYear(theMonth(mMonth) + ", " + mYear);
    }

    @Override public void initializeViews() {
        mGridCalendarView.initializeToolbar();
        mGridCalendarView.initializeEmptyRelativeLayout();
        mGridCalendarView.initializeRecyclerLayoutManager(
                new GridLayoutManager(mGridCalendarView.getActivity(), 7));
        mGridCalendarView.initializeBasePageView();
    }

    @Override public void saveState(Bundle outState) {
        if (mGridCalendarMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mGridCalendarMapper.getPositionState());
            outState.putInt(CURRENT_MONTH, mMonth);
            outState.putInt(CURRENT_YEAR, mYear);
        }
    }

    @Override public void restoreState(Bundle savedState) {
        if (savedState.containsKey(POSITION_PARCELABLE_KEY)) {
            mPositionSavedState = savedState.getParcelable(POSITION_PARCELABLE_KEY);
            mYear = savedState.getInt(CURRENT_YEAR);
            mMonth = savedState.getInt(CURRENT_MONTH);
            savedState.remove(POSITION_PARCELABLE_KEY);
        }
    }

    @Override public void nextMonth() {
        if (mMonth == 11) {
            mYear += 1;
            mMonth = 0;
        } else {
            mMonth += 1;
        }
        initializeDataFromPreferenceSource();
    }

    @Override public void previousMonth() {
        if (mMonth == 0) {
            mYear -= 1;
            mMonth = 11;
        } else {
            mMonth -= 1;
        }
        initializeDataFromPreferenceSource();
    }

    @Override public void releaseAllResources() {
        if (mGridCalendarAdapter != null) {
            mGridCalendarAdapter = null;
        }
    }

    @Override public void restorePosition() {
        if (mPositionSavedState != null) {
            mGridCalendarMapper.setPositionState(mPositionSavedState);

            mPositionSavedState = null;
        }
    }

    @Override public void changeGrid(Grid grid) {
        currentGrid = grid;
        initializeDataFromPreferenceSource();
    }
}
