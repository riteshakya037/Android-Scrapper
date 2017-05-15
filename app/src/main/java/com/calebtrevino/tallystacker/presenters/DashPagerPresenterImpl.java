package com.calebtrevino.tallystacker.presenters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.calebtrevino.tallystacker.controllers.receivers.GameUpdateReceiver;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.database.DatabaseTask;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;
import com.calebtrevino.tallystacker.presenters.events.DashCountEvent;
import com.calebtrevino.tallystacker.presenters.mapper.DashPagerMapper;
import com.calebtrevino.tallystacker.views.DashPagerView;
import com.calebtrevino.tallystacker.views.adaptors.DashAdapter;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

/**
 * Created by Ritesh on 0012, May 12, 2017.
 */

public class DashPagerPresenterImpl implements DashPagerPresenter, ChildGameEventListener {
    private static final String TAG = DashPagerPresenterImpl.class.getSimpleName();
    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private final DashPagerView mView;
    private final DashPagerMapper mMapper;
    private DashAdapter mDashAdapter;
    private DatabaseContract.DbHelper dbHelper;

    public DashPagerPresenterImpl(DashPagerView mView, DashPagerMapper mMapper) {
        this.mView = mView;
        this.mMapper = mMapper;
    }

    @Override
    public void onChildChanged(final Game game) {
        mDashAdapter.modify(game);
    }

    @Override
    public void onChildRemoved(final Game game) {
        mDashAdapter.removeCard(game);
    }

    @Override
    public void onChildAdded(final Game game) {
        if (DatabaseContract.checkGameValidity(game, mMapper.getPosition())) {
            if (mDashAdapter != null)
                mDashAdapter.addGame(game);
            if (new DateTime(game.getGameDateTime(), DateTimeZone.getDefault()).plusMinutes(game.getLeagueType().getAvgTime()).isBeforeNow() && game.getGameStatus() == GameStatus.NEUTRAL) {
                Intent gameIntent = new Intent(mView.getActivity(), GameUpdateReceiver.class);
                Log.i(TAG, "onChildAdded: Should Have completed game " + game.get_id());
                gameIntent.putExtra("game", game.get_id());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mView.getActivity(), (int) game.get_id(), gameIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                long interval = game.getLeagueType().getRefreshInterval() * 60 * 1000L;
                AlarmManager manager = (AlarmManager) mView.getActivity().getSystemService(Context.ALARM_SERVICE);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, new DateTime().getMillis(), interval, pendingIntent);
            }
        }
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mView.getActivity());
    }

    @Override
    public void initializeViews() {
        mView.initializeRecyclerLayoutManager(new LinearLayoutManager(mView.getActivity()));
    }

    @Override
    public void saveState(Bundle outState) {
        if (mMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mMapper.getPositionState());
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        if (savedState.containsKey(POSITION_PARCELABLE_KEY)) {
            savedState.remove(POSITION_PARCELABLE_KEY);
        }
    }

    @Override
    public void releaseAllResources() {
        if (mDashAdapter != null) {
            mDashAdapter = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void restorePosition() {
        if (mDashAdapter != null) {
            mDashAdapter = null;
        }
    }

    @Override
    public void isEmpty(boolean isEmpty) {
        if (isEmpty) {
            mView.showEmptyRelativeLayout();
        } else {
            mView.hideEmptyRelativeLayout();
        }
    }


    @Override
    public void initializeDataFromPreferenceSource() {
        mDashAdapter = new DashAdapter(mView.getActivity(), mMapper.getPosition());
        mMapper.registerAdapter(mDashAdapter);
        mDashAdapter.setNullListener(this);
        new DatabaseTask<List<Game>>(dbHelper) {
            @Override
            protected void callInUI(List<Game> o) {
            }

            @Override
            protected List<Game> executeStatement(DatabaseContract.DbHelper dbHelper) {
                return dbHelper.selectUpcomingGames(mMapper.getPosition());
            }
        }.execute();
    }

    public void spinnerClicked(int position) {
        if (position == 0) {
            mDashAdapter.changeSort(new Game.GameTimeComparator());
        } else if (position == 1) {
            mDashAdapter.changeSort(new Game.GameComparator());
        } else {
            mDashAdapter.changeSort(new Game.VIComparator());
        }
    }

    public void pushEvent() {
        EventBus.getDefault().post(new DashCountEvent(mDashAdapter.getItemCount(), mMapper.getPosition()));
    }
}
