package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.League;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
import com.calebtrevino.tallystacker.presenters.mapper.LeagueMapper;
import com.calebtrevino.tallystacker.views.LeagueView;
import com.calebtrevino.tallystacker.views.adaptors.LeagueFragmentPagerAdapter;

import java.util.List;

/**
 * Created by fatal on 9/5/2016.
 */
public class LeaguePresenterImpl implements LeaguePresenter {
    public static final String TAG = GridPagePresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private LeagueFragmentPagerAdapter mPagerAdapter;
    private Parcelable mPositionSavedState;

    private LeagueView mLeagueView;
    private LeagueMapper mLeagueMapper;
    private DatabaseContract.DbHelper dbHelper;

    public LeaguePresenterImpl(LeagueView leagueView, LeagueMapper leagueMapper) {
        mLeagueView = leagueView;
        mLeagueMapper = leagueMapper;
    }

    @Override
    public void initializeViews() {
        mLeagueView.initializeToolbar();
        mLeagueView.initializeBasePageView();
        mLeagueView.initializeEmptyRelativeLayout();
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mLeagueView.getActivity());
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        new DatabaseTask(dbHelper) {
            @Override
            protected void callInUI(Object o) {
                mPagerAdapter = new LeagueFragmentPagerAdapter(mLeagueView.getFragmentManager(), (List<League>) o);
                mLeagueView.hideEmptyRelativeLayout();
                mLeagueMapper.registerAdapter(mPagerAdapter);
            }

            @Override
            protected List<League> executeStatement(DatabaseContract.DbHelper dbHelper) {
                return dbHelper.getLeagues();
            }
        }.execute();
    }

    @Override
    public void saveState(Bundle outState) {
        if (mLeagueMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mLeagueMapper.getPositionState());
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
        if (mPagerAdapter != null) {
            mPagerAdapter = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void restorePosition() {

    }
}
