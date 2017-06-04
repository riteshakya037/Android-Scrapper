package com.calebtrevino.tallystacker.views.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.ServiceInterface;
import com.calebtrevino.tallystacker.ServiceListener;
import com.calebtrevino.tallystacker.controllers.events.DashPageSwipeEvent;
import com.calebtrevino.tallystacker.controllers.events.GameAddedEvent;
import com.calebtrevino.tallystacker.controllers.events.GameModifiedEvent;
import com.calebtrevino.tallystacker.controllers.services.ScrapperService;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.preferences.MultiProcessPreference;
import com.calebtrevino.tallystacker.presenters.DashPresenterImpl;
import com.calebtrevino.tallystacker.presenters.events.DashCountEvent;
import com.calebtrevino.tallystacker.presenters.events.ErrorEvent;
import com.calebtrevino.tallystacker.presenters.mapper.DashMapper;
import com.calebtrevino.tallystacker.utils.DateUtils;
import com.calebtrevino.tallystacker.utils.LogWriter;
import com.calebtrevino.tallystacker.utils.StringUtils;
import com.calebtrevino.tallystacker.views.DashView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashFragment extends Fragment implements DashView, DashMapper, ViewPager.OnPageChangeListener {
    public static final String LOG_FILE_LOCATION = "log_file_location";
    private static final String TAG = DashFragment.class.getSimpleName();
    private final ServiceListener.Stub serviceListener = new ServiceListener.Stub() {
        @Override
        public void gameAdded(Game game) throws RemoteException {
            EventBus.getDefault().post(new GameAddedEvent(game));
        }

        @Override
        public void gameModified(Game game) throws RemoteException {
            EventBus.getDefault().post(new GameModifiedEvent(game));
        }

        @Override
        public void gameDeleted(Game game) throws RemoteException {
            // Empty method
        }
    };
    @BindView(R.id.fragment_dash_pager)
    ViewPager mViewPager;
    @BindView(R.id.fragment_dash_send_button)
    FloatingActionButton sendButton;

    @SuppressWarnings("WeakerAccess")
    ServiceInterface serviceInterface;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceInterface = ServiceInterface.Stub.asInterface(service);
            try {
                serviceInterface.addListener(serviceListener);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to add listener", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Empty method
        }
    };
    private DashPresenterImpl dashPresenter;
    private Spinner mSpinner;

    @OnClick(R.id.fragment_dash_send_button)
    void sendError() {
        LogWriter.sendLog(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashPresenter = new DashPresenterImpl(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getContext(), ScrapperService.class);
        getActivity().bindService(i, serviceConnection, 0);
        EventBus.getDefault().post(new ErrorEvent(!StringUtils.isNull(MultiProcessPreference.getDefaultSharedPreferences().getString(LOG_FILE_LOCATION, ""))));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View gridFrag = inflater.inflate(R.layout.fragment_dash, container, false);
        ButterKnife.bind(this, gridFrag);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        MenuItem item = toolbar.getMenu().findItem(R.id.spinner);
        if (item != null)
            mSpinner = (Spinner) MenuItemCompat.getActionView(item);
        return gridFrag;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dashPresenter.initializeViews();
        dashPresenter.initializeSpinner();
        dashPresenter.initializeData();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_dash);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.upcoming_games);
        }
    }

    @Override
    public void onPause() {
        try {
            if (serviceInterface != null) {
                serviceInterface.removeListener(serviceListener);
                getActivity().unbindService(serviceConnection);
            }
        } catch (Throwable t) {
            Log.w(TAG, "Failed to unbind from the service", t);
        }
        super.onPause();
    }


    @Override
    public Context getContext() {
        return getActivity();
    }


    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleEvent(DashCountEvent event) {
        if (event.getDateLag() == mViewPager.getCurrentItem()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.dash_title, getString(R.string.fragment_dash), event.getSize()));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(event.getDateLag() == 0 ? "Today" : event.getDateLag() == 1 ? "Yesterday" : DateUtils.getDateMinus("MMM dd", event.getDateLag()));
        }
    }

    @Override
    public void registerSpinner(ArrayAdapter<String> adapter) {
        if (mSpinner != null) {
            mSpinner.setAdapter(adapter);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onError(ErrorEvent event) {
        sendButton.setVisibility(event.isVisible() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initializeSpinnerListener() {
        if (mSpinner != null) {
            SpinnerInteractionListener interactionListener = new SpinnerInteractionListener();
            mSpinner.setOnTouchListener(interactionListener);
            mSpinner.setOnItemSelectedListener(interactionListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void registerAdapter(FragmentStatePagerAdapter adapter) {
        if (mViewPager != null) {
            mViewPager.setAdapter(adapter);
        }
    }

    @Override
    public void initializeBasePageView() {
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Empty method
    }

    @Override
    public void onPageSelected(int position) {
        EventBus.getDefault().post(new DashPageSwipeEvent(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Empty method
    }


    private class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                dashPresenter.spinnerClicked(position);
            }
            userSelect = false;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
