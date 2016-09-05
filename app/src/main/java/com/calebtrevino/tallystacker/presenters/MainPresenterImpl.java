package com.calebtrevino.tallystacker.presenters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.calebtrevino.tallystacker.utils.NavigationUtils;
import com.calebtrevino.tallystacker.views.fragments.DashFragment;
import com.calebtrevino.tallystacker.views.MainView;
import com.calebtrevino.tallystacker.views.activities.MainActivity;


public class MainPresenterImpl implements MainPresenter {
    public static final String TAG = MainPresenterImpl.class.getSimpleName();

    private static final String MAIN_FRAGMENT_PARCELABLE_KEY = TAG + ":" + "MainFragmentParcelableKey";

    private MainView mMainView;

    private Fragment mFragment;

    private int mInitialPosition;


    public MainPresenterImpl(MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void initializeViews() {
        mMainView.initializeToolbar();
        mMainView.initializeDrawerLayout();
    }

    @Override
    public void initializeMainLayout(Intent argument) {
        if (argument != null) {
            if (argument.hasExtra(MainActivity.POSITION_ARGUMENT_KEY)) {
                mInitialPosition = argument.getIntExtra(MainActivity.POSITION_ARGUMENT_KEY, NavigationUtils.POSITION_DASHBOARD);

                if (mInitialPosition == NavigationUtils.POSITION_DASHBOARD) {
                    mFragment = new DashFragment();
                }
                argument.removeExtra(MainActivity.POSITION_ARGUMENT_KEY);
            }
        }

        if (mFragment == null) {
            mInitialPosition = NavigationUtils.POSITION_DASHBOARD;

            mFragment = new DashFragment();
        }

        ((FragmentActivity) mMainView.getContext()).getSupportFragmentManager().beginTransaction()
                .add(mMainView.getMainLayoutId(), mFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        System.out.println("ENTERED");
        int id = item.getItemId();

        if (id == NavigationUtils.POSITION_DASHBOARD) {
            onPositionCatalogue();
        }

        mMainView.closeDrawerLayout();
        return true;
    }


    @Override
    public void saveState(Bundle outState) {
        if (mFragment != null) {
            ((FragmentActivity) mMainView.getContext()).getSupportFragmentManager().putFragment(outState, MAIN_FRAGMENT_PARCELABLE_KEY, mFragment);
        }
    }

    @Override
    public void restoreState(Bundle savedState) {
        if (savedState.containsKey(MAIN_FRAGMENT_PARCELABLE_KEY)) {
            mFragment = ((FragmentActivity) mMainView.getContext()).getSupportFragmentManager().getFragment(savedState, MAIN_FRAGMENT_PARCELABLE_KEY);

            savedState.remove(MAIN_FRAGMENT_PARCELABLE_KEY);
        }
    }


    private void onPositionCatalogue() {
        mFragment = new DashFragment();

        replaceMainFragment();
    }


    private void replaceMainFragment() {
        ((FragmentActivity) mMainView.getContext()).getSupportFragmentManager().beginTransaction()
                .replace(mMainView.getMainLayoutId(), mFragment)
                .commit();
    }

}
