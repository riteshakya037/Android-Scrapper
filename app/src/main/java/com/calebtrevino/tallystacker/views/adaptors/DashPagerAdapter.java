package com.calebtrevino.tallystacker.views.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.calebtrevino.tallystacker.views.fragments.DashPagerFragment;

/**
 * @author Ritesh Shakya
 */

public class DashPagerAdapter extends FragmentStatePagerAdapter {

    public DashPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DashPagerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}