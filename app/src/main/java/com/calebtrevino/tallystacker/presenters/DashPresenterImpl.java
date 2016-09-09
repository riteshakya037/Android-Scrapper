package com.calebtrevino.tallystacker.presenters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.base.BaseModel;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.listeners.ChildGameEventListener;
import com.calebtrevino.tallystacker.presenters.mapper.DashMapper;
import com.calebtrevino.tallystacker.views.DashView;
import com.calebtrevino.tallystacker.views.adaptors.DashAdapter;

/**
 * Created by fatal on 9/9/2016.
 */
public class DashPresenterImpl implements DashPresenter {

    public static final String TAG = DashPresenterImpl.class.getSimpleName();

    private static final String POSITION_PARCELABLE_KEY = TAG + ":" + "PositionParcelableKey";
    private final DashView mDashView;
    private final DashMapper mDashMapper;
    private Parcelable mPositionSavedState;
    private DashAdapter mDashAdapter;

    public DashPresenterImpl(DashView dashView, DashMapper dashMapper) {
        this.mDashView = dashView;
        this.mDashMapper = dashMapper;
    }

    @Override
    public void initializeViews() {
        mDashView.initializeToolbar();
        mDashView.initializeEmptyRelativeLayout();
        mDashView.initializeRecyclerLayoutManager(new LinearLayoutManager(mDashView.getActivity()));
        mDashView.initializeBasePageView();
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        mDashAdapter = new DashAdapter(mDashView.getActivity());
        mDashMapper.registerAdapter(mDashAdapter);
        mDashAdapter.setNullListener(this);
        DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(mDashView.getActivity());
        dbHelper.addChildGameEventListener(new ChildGameEventListener() {
            @Override
            public void onChildAdded(BaseModel baseModel) {
                mDashAdapter.addGame((Game) baseModel);
            }

            @Override
            public void onChildChanged(BaseModel baseModel) {
//                mDashAdapter.changeame((Game) baseModel);
            }

            @Override
            public void onChildRemoved(BaseModel baseModel) {
                mDashAdapter.removeCard((Game) baseModel);
            }

        });
        dbHelper.selectRecentGames(10);
    }

    @Override
    public void saveState(Bundle outState) {
        if (mDashMapper.getPositionState() != null) {
            outState.putParcelable(POSITION_PARCELABLE_KEY, mDashMapper.getPositionState());
        }
    }

    @Override
    public void releaseAllResources() {

    }

    @Override
    public void restorePosition() {
        if (mDashAdapter != null) {
            mDashAdapter = null;
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
    public void isEmpty(boolean isEmpty) {
        if (isEmpty) {
            mDashView.showEmptyRelativeLayout();
        } else {
            mDashView.hideEmptyRelativeLayout();
        }
    }
}
