package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;

import com.calebtrevino.tallystacker.controllers.sources.bases.League;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
import com.calebtrevino.tallystacker.presenters.mapper.LeagueMapper;
import com.calebtrevino.tallystacker.views.LeagueView;
import com.calebtrevino.tallystacker.views.adaptors.LeagueFragmentPagerAdapter;

import java.util.List;

/**
 * @author Ritesh Shakya
 */
public class LeaguePresenterImpl implements LeaguePresenter {
    private static final String TAG = GridPagePresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";

    private LeagueFragmentPagerAdapter mPagerAdapter;
    private Parcelable mPositionSavedState;

    private final LeagueView mLeagueView;
    private final LeagueMapper mLeagueMapper;
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
