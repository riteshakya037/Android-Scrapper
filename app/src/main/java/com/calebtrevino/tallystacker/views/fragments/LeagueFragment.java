package com.calebtrevino.tallystacker.views.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.presenters.LeaguePresenter;
import com.calebtrevino.tallystacker.presenters.LeaguePresenterImpl;
import com.calebtrevino.tallystacker.views.bases.BaseEmptyRelativeLayoutView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueFragment extends Fragment implements BaseToolbarView {
    public static final String TAG = LeagueFragment.class.getSimpleName();

    private LeaguePresenter mLeaguePresenter;

    private Parcelable mPositionSavedState;

    public LeagueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mLeaguePresenter = new LeaguePresenterImpl(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View leagueFrag = inflater.inflate(R.layout.fragment_league, container, false);
        ButterKnife.bind(leagueFrag);
        return leagueFrag;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLeaguePresenter.restoreState(savedInstanceState);
        }

        mLeaguePresenter.initializeViews();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLeaguePresenter.saveState(outState);
    }

    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_leagues);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }

    private void restorePosition() {
        if (mPositionSavedState != null) {

            mPositionSavedState = null;
        }
    }
}
