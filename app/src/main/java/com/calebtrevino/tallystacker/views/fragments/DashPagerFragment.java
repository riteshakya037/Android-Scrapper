package com.calebtrevino.tallystacker.views.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.events.DashPageSwipeEvent;
import com.calebtrevino.tallystacker.controllers.events.GameAddedEvent;
import com.calebtrevino.tallystacker.controllers.events.GameModifiedEvent;
import com.calebtrevino.tallystacker.controllers.events.SpinnerEvent;
import com.calebtrevino.tallystacker.presenters.DashPagerPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.DashPagerMapper;
import com.calebtrevino.tallystacker.views.DashPagerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Ritesh Shakya
 */

public class DashPagerFragment extends Fragment implements DashPagerView, DashPagerMapper {

    private static final String DATE_LAG = "date_lag";
    @BindView(R.id.dashViewRecycler) protected RecyclerView mDashRecycler;
    @BindView(R.id.emptyRelativeLayout) protected RelativeLayout mEmptyRelativeLayout;
    private DashPagerPresenterImpl dashPagerPresenter;

    public static DashPagerFragment newInstance(int position) {
        DashPagerFragment fragment = new DashPagerFragment();
        Bundle args = new Bundle();
        args.putInt(DATE_LAG, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashPagerPresenter = new DashPagerPresenterImpl(this, this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View gridFrag = inflater.inflate(R.layout.item_dash_pager, container, false);
        ButterKnife.bind(this, gridFrag);
        return gridFrag;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dashPagerPresenter.initializeViews();
        if (savedInstanceState != null) {
            dashPagerPresenter.restoreState(savedInstanceState);
        }

        dashPagerPresenter.initializeDatabase();
        dashPagerPresenter.initializeDataFromPreferenceSource();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        dashPagerPresenter.releaseAllResources();
    }

    @Override public void initializeEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            ((ImageView) mEmptyRelativeLayout.findViewById(R.id.emptyImageView)).setImageResource(
                    R.drawable.empty_grid);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.emptyTextView)).setText(
                    R.string.no_games);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.instructionsTextView)).setText(
                    isNetworkConnected() ? R.string.fetching_data
                            : R.string.no_internet_connection);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override public void hideEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override public void showEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override public Parcelable getPositionState() {
        if (mDashRecycler != null) {
            return mDashRecycler.getLayoutManager().onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override public void setPositionState(Parcelable state) {
        if (mDashRecycler != null) {
            mDashRecycler.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override public void registerAdapter(RecyclerView.Adapter<?> adapter) {
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

    @SuppressWarnings("unused") @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameAdd(GameAddedEvent event) {
        dashPagerPresenter.onChildAdded(event.getGameData());
    }

    @SuppressWarnings("unused") @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameModified(GameModifiedEvent event) {
        dashPagerPresenter.onChildChanged(event.getGameData());
    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused") @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameModified(SpinnerEvent event) {
        dashPagerPresenter.spinnerClicked(event.getPosition());
    }

    @SuppressWarnings("unused") @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPageSelected(DashPageSwipeEvent event) {
        if (event.getPosition() == getPosition()) dashPagerPresenter.pushEvent();
    }

    @Override public int getPosition() {
        return getArguments().getInt(DATE_LAG, 0);
    }
}
