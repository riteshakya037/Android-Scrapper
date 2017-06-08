package com.calebtrevino.tallystacker.views.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.views.adaptors.GridDialogCalendarAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class GridCalendarDialog extends DialogFragment {

    private static final String DATA_MAP = "data_map";
    private static final String POSITION = "current_date";
    private static final String COUNT_MAP = "count_map";
    @BindView(R.id.container)
    protected ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    protected TabLayout mTabLayout;
    private HashMap<Long, ArrayList<Game>> data;
    private HashMap<Integer, Long> countMapping;
    private long currentDate;

    public static DialogFragment newInstance(HashMap<Long, List<Game>> data, HashMap<Integer, Long> countMapping, long position) {
        GridCalendarDialog fragment = new GridCalendarDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATA_MAP, data);
        args.putSerializable(COUNT_MAP, countMapping);
        args.putLong(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_grid_view, container);
        ButterKnife.bind(this, view);

        GridDialogCalendarAdapter adapter = new GridDialogCalendarAdapter(getChildFragmentManager(), getContext());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        adapter.setData(data, countMapping);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
            }
        }
        for (Map.Entry<Integer, Long> entry : countMapping.entrySet()) {
            if (entry.getValue() == currentDate) {
                mViewPager.setCurrentItem(entry.getKey());
            }
        }
        //A little space between pages
        mViewPager.setPageMargin(15);

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        mViewPager.setClipChildren(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (HashMap<Long, ArrayList<Game>>) getArguments().getSerializable(DATA_MAP);
            currentDate = getArguments().getLong(POSITION);
            countMapping = (HashMap<Integer, Long>) getArguments().getSerializable(COUNT_MAP);
        }
    }
}
