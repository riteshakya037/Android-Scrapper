package com.calebtrevino.tallystacker.views.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.presenters.DashPresenter;
import com.calebtrevino.tallystacker.presenters.DashPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.DashMapper;
import com.calebtrevino.tallystacker.views.DashView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashFragment extends Fragment implements DashView, DashMapper {

    private DashPresenter dashPresenter;

    @BindView(R.id.dashViewRecycler)
    RecyclerView mDashRecycler;


    @BindView(R.id.emptyRelativeLayout)
    RelativeLayout mEmptyRelativeLayout;
    private Handler mUIHandler;


    public DashFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUIHandler = new Handler();

        dashPresenter = new DashPresenterImpl(this, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View gridFrag = inflater.inflate(R.layout.fragment_dash, container, false);
        ButterKnife.bind(this, gridFrag);

        return gridFrag;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            dashPresenter.restoreState(savedInstanceState);
        }

        dashPresenter.initializeViews();
        dashPresenter.initializeDatabase();
        dashPresenter.initializeDataFromPreferenceSource();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        dashPresenter.saveState(outState);
    }

    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_dash);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.upcoming_games);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dashPresenter.releaseAllResources();
    }

    @Override
    public void initializeEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            ((ImageView) mEmptyRelativeLayout.findViewById(R.id.emptyImageView)).setImageResource(R.drawable.empty_grid);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.emptyTextView)).setText(R.string.no_games);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.instructionsTextView)).setText(isNetworkConnected() ? R.string.fetching_data : R.string.no_internet_connection);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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
    public Parcelable getPositionState() {
        if (mDashRecycler != null) {
            return mDashRecycler.getLayoutManager().onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override
    public void setPositionState(Parcelable state) {
        if (mDashRecycler != null) {
            mDashRecycler.getLayoutManager().onRestoreInstanceState(state);
        }
    }


    @Override
    public void registerAdapter(RecyclerView.Adapter<?> adapter) {
        if (mDashRecycler != null) {
            mDashRecycler.setAdapter(adapter);
        }
    }

    @Override
    public void initializeRecyclerLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mDashRecycler != null) {
            mDashRecycler.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void initializeBasePageView() {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void handleInMainUI(Runnable runnable) {
        if (mUIHandler != null) {
            mUIHandler.post(runnable);
        }
    }
}
