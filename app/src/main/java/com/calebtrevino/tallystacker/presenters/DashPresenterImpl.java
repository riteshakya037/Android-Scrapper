package com.calebtrevino.tallystacker.presenters;

import android.widget.ArrayAdapter;
import com.calebtrevino.tallystacker.controllers.events.SpinnerEvent;
import com.calebtrevino.tallystacker.presenters.mapper.DashMapper;
import com.calebtrevino.tallystacker.views.DashView;
import com.calebtrevino.tallystacker.views.adaptors.DashPagerAdapter;
import org.greenrobot.eventbus.EventBus;

/**
 * @author Ritesh Shakya
 */
public class DashPresenterImpl implements DashPresenter {

    private final DashView mDashView;
    private final DashMapper mDashMapper;

    public DashPresenterImpl(DashView dashView, DashMapper dashMapper) {
        this.mDashView = dashView;
        this.mDashMapper = dashMapper;
    }

    @Override public void initializeViews() {
        mDashView.initializeToolbar();
        mDashView.initializeBasePageView();
        mDashMapper.initializeSpinnerListener();
    }

    @Override public void initializeSpinner() {
        ArrayAdapter<String> mSpinnerAdapter =
                new ArrayAdapter<>(mDashView.getActivity(), android.R.layout.simple_spinner_item,
                        new String[] { "VI", "League", "Time" });
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDashMapper.registerSpinner(mSpinnerAdapter);
    }

    @Override public void spinnerClicked(int position) {
        EventBus.getDefault().post(new SpinnerEvent(position));
    }

    public void initializeData() {
        DashPagerAdapter mPagerAdapter = new DashPagerAdapter(mDashView.getFragmentManager());
        mDashMapper.registerAdapter(mPagerAdapter);
    }
}
