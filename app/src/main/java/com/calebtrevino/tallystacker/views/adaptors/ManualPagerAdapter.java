package com.calebtrevino.tallystacker.views.adaptors;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.views.fragments.ManualItemFragment;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */
public class ManualPagerAdapter extends FragmentStatePagerAdapter {

    private final List<ManualItemFragment> saveListeners;
    private List<Game> mData;

    public ManualPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mData = new LinkedList<>();
        this.saveListeners = new LinkedList<>();
    }

    @Override public Fragment getItem(int position) {
        ManualItemFragment fragment = ManualItemFragment.newInstance(mData.get(position));
        if (!saveListeners.contains(fragment)) {
            saveListeners.add(fragment);
        }
        return fragment;
    }

    @Override public int getCount() {
        return mData.size();
    }

    public void setData(List<Game> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public ManualItemFragment getFragment(int currentItem) {
        return saveListeners.get(currentItem);
    }

    @Override public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    public void remove(int currentItem) {
        saveListeners.remove(currentItem);
        this.mData.remove(currentItem);
        this.notifyDataSetChanged();
    }
}
