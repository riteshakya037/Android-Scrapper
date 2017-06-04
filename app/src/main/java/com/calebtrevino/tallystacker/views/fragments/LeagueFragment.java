package com.calebtrevino.tallystacker.views.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.presenters.LeaguePresenter;
import com.calebtrevino.tallystacker.presenters.LeaguePresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.LeagueMapper;
import com.calebtrevino.tallystacker.views.LeagueView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueFragment extends Fragment implements LeagueView, LeagueMapper {
    @SuppressWarnings("unused")
    public static final String TAG = LeagueFragment.class.getSimpleName();

    private LeaguePresenter mLeaguePresenter;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.emptyRelativeLayout)
    RelativeLayout mEmptyRelativeLayout;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.container)
    ViewPager mViewPager;
    private Handler mUIHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUIHandler = new Handler();

        mLeaguePresenter = new LeaguePresenterImpl(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View leagueFrag = inflater.inflate(R.layout.fragment_league, container, false);
        ButterKnife.bind(this, leagueFrag);
        return leagueFrag;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLeaguePresenter.initializeViews();
        if (savedInstanceState != null) {
            mLeaguePresenter.restoreState(savedInstanceState);
        }
        mLeaguePresenter.initializeDatabase();
        mLeaguePresenter.initializeDataFromPreferenceSource();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLeaguePresenter.saveState(outState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_leagues);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }


    @Override
    public void registerAdapter(FragmentStatePagerAdapter adapter) {
        if (mViewPager != null) {
            mViewPager.setAdapter(adapter);
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public Parcelable getPositionState() {
        if (mViewPager != null) {
            return mViewPager.onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override
    public void setPositionState(Parcelable state) {
        if (mViewPager != null) {
            mViewPager.onRestoreInstanceState(state);
        }
    }

    @Override
    public void initializeEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            ((ImageView) mEmptyRelativeLayout.findViewById(R.id.emptyImageView)).setImageResource(R.drawable.empty_grid);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.emptyTextView)).setText(R.string.loading_from_database);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.instructionsTextView)).setText(R.string.please_wait);
        }
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
        // Empty method
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLeaguePresenter.releaseAllResources();
    }

    @Override
    public void handleInMainUI(Runnable runnable) {
        if (mUIHandler != null) {
            mUIHandler.post(runnable);
        }
    }
}
