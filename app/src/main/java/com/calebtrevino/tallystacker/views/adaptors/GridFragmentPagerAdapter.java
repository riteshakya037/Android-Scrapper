package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.listeners.GridChangeListener;
import com.calebtrevino.tallystacker.views.fragments.GridCalenderFragment;
import com.calebtrevino.tallystacker.views.fragments.GridHolderFragment;
import com.calebtrevino.tallystacker.views.fragments.GridSettingFragment;
import com.calebtrevino.tallystacker.views.fragments.GridViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */

public class GridFragmentPagerAdapter extends FragmentStatePagerAdapter {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.icon)
    ImageView icon;

    private final List<GridChangeListener> gridChangeListeners;

    private final String[] mTabsTitle = {"Grids", "Calendar", "Setting"};

    private final int[] mTabsIcons = {
            R.drawable.ic_view_module_white_24px,
            R.drawable.ic_date_range_white_24px,
            R.drawable.ic_settings_white_24px};
    private final Context mContext;

    public GridFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        gridChangeListeners = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        GridHolderFragment fragment;
        switch (position) {
            case 0:
                fragment = GridViewFragment.newInstance();
                break;
            case 1:
                fragment = GridCalenderFragment.newInstance();
                break;
            case 2:
                fragment = GridSettingFragment.newInstance();
                break;
            default:
                fragment = GridViewFragment.newInstance();
        }
        if (!gridChangeListeners.contains(fragment.getGridChangeListener())) {
            gridChangeListeners.add(fragment.getGridChangeListener());
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabsTitle[position];
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        ButterKnife.bind(this, view);
        title.setText(mTabsTitle[position]);
        icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mTabsIcons[position]);
        return view;
    }

    public void changeTo(Grid grid) {
        for (GridChangeListener listener : gridChangeListeners) {
            listener.added(grid);
        }
    }
}