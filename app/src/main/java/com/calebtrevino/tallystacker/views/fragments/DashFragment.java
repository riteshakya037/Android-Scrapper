package com.calebtrevino.tallystacker.views.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.ServiceInterface;
import com.calebtrevino.tallystacker.ServiceListener;
import com.calebtrevino.tallystacker.controllers.services.ScrapperService;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.presenters.DashPresenterImpl;
import com.calebtrevino.tallystacker.presenters.events.DashCountEvent;
import com.calebtrevino.tallystacker.presenters.mapper.DashMapper;
import com.calebtrevino.tallystacker.views.DashView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashFragment extends Fragment implements DashView, DashMapper {
    private static final String TAG = DashFragment.class.getSimpleName();

    private DashPresenterImpl dashPresenter;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.dashViewRecycler)
    RecyclerView mDashRecycler;


    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.emptyRelativeLayout)
    RelativeLayout mEmptyRelativeLayout;
    private Handler mUIHandler;
    @SuppressWarnings("WeakerAccess")
    ServiceInterface serviceInterface;

    private Spinner mSpinner;

    private final ServiceListener.Stub serviceListener = new ServiceListener.Stub() {
        @Override
        public void databaseReady(Game game) throws RemoteException {
            if (dashPresenter != null) {
                dashPresenter.onChildAdded(game);
            }
        }
    };
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
            dashPresenter.initializeDataFromPreferenceSource();
        }
    };


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
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getContext(), ScrapperService.class);
        getActivity().bindService(i, serviceConnection, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View gridFrag = inflater.inflate(R.layout.fragment_dash, container, false);
        ButterKnife.bind(this, gridFrag);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        MenuItem item = toolbar.getMenu().findItem(R.id.spinner);
        mSpinner = (Spinner) MenuItemCompat.getActionView(item);
        return gridFrag;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dashPresenter.initializeViews();
        dashPresenter.initializeSpinner();
        if (savedInstanceState != null) {
            dashPresenter.restoreState(savedInstanceState);
        }

        dashPresenter.initializeDatabase();
        dashPresenter.initializeDataFromPreferenceSource();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        dashPresenter.saveState(outState);
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
            serviceInterface.removeListener(serviceListener);
            getActivity().unbindService(serviceConnection);
        } catch (Throwable t) {
            Log.w(TAG, "Failed to unbind from the service", t);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Fragment destroying");
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

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleEvent(DashCountEvent event) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.dash_title, getString(R.string.fragment_dash), event.getSize()));
    }

    @Override
    public void registerSpinner(ArrayAdapter<String> adapter) {
        if (mSpinner != null) {
            mSpinner.setAdapter(adapter);
        }
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

    class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
