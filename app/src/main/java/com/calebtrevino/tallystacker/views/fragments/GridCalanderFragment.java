package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.presenters.GridCalendarPresenter;
import com.calebtrevino.tallystacker.presenters.GridCalendarPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.GridCalendarMapper;
import com.calebtrevino.tallystacker.views.GridCalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridCalanderFragment extends GridHolderFragment implements GridCalendarView, GridCalendarMapper {
    @BindView(R.id.emptyRelativeLayout)
    RelativeLayout mEmptyRelativeLayout;

    @BindView(R.id.calendarView)
    RecyclerView mCalendarRecycleView;

    @BindView(R.id.monthText)
    TextView monthYearText;

    GridCalendarPresenter gridCalendarPresenter;

    @OnClick(R.id.previousMonth)
    void previousMonth() {
        gridCalendarPresenter.previousMonth();
    }


    @OnClick(R.id.nextMonth)
    void nextMonth() {
        gridCalendarPresenter.nextMonth();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        gridCalendarPresenter = new GridCalendarPresenterImpl(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            gridCalendarPresenter.restoreState(savedInstanceState);
        }
        gridCalendarPresenter.initializeData();
        gridCalendarPresenter.initializeViews();
        gridCalendarPresenter.initializeDataFromPreferenceSource();
    }


    public static GridHolderFragment newInstance() {
        return new GridCalanderFragment();
    }

    @Override
    public void initializeEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            ((ImageView) mEmptyRelativeLayout.findViewById(R.id.emptyImageView)).setImageResource(R.drawable.empty_grid);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.emptyTextView)).setText(R.string.no_games);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.instructionsTextView)).setText(R.string.fetch_games);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        gridCalendarPresenter.releaseAllResources();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gridCalendarPresenter.saveState(outState);
    }


    @Override
    public void hideEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initializeBasePageView() {

    }

    @Override
    public void initializeRecyclerLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mCalendarRecycleView != null) {
            mCalendarRecycleView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_grid);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }


    @Override
    public Parcelable getPositionState() {
        if (mCalendarRecycleView != null) {
            return mCalendarRecycleView.getLayoutManager().onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override
    public void setPositionState(Parcelable state) {
        if (mCalendarRecycleView != null) {
            mCalendarRecycleView.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    public void setMonthYear(String monthYear) {
        monthYearText.setText(monthYear);
    }

    @Override
    public void registerAdapter(RecyclerView.Adapter<?> adapter) {
        if (mCalendarRecycleView != null) {
            mCalendarRecycleView.setAdapter(adapter);
        }
    }
}
