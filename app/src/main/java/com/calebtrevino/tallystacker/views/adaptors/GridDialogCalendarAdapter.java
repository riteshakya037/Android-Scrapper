package com.calebtrevino.tallystacker.views.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.views.fragments.GridDialogCalendarFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */

public class GridDialogCalendarAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    @BindView(R.id.dateText)
    protected TextView dateText;
    private HashMap<Long, ArrayList<Game>> mData;
    private HashMap<Integer, Long> countMapping;

    public GridDialogCalendarAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        this.mData = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        long currentDate = 0;
        for (Map.Entry<Integer, Long> entry : countMapping.entrySet()) {
            if (entry.getKey() == position) {
                currentDate = entry.getValue();
            }
        }
        return GridDialogCalendarFragment.newInstance(mData.get(currentDate));
    }

    @Override
    public int getCount() {
        return mData.size();
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.grd_calendar_dialog_item, null);
        ButterKnife.bind(this, view);

        long currentDate = 0;
        for (Map.Entry<Integer, Long> entry : countMapping.entrySet()) {
            if (entry.getKey() == position) {
                currentDate = entry.getValue();
            }
        }
        dateText.setText(DateTimeFormat.forPattern("MMM dd\nEEE").print(new DateTime(currentDate)));
        return view;
    }

    public void setData(HashMap<Long, ArrayList<Game>> data, HashMap<Integer, Long> countMapping) {
        this.mData = data;
        this.countMapping = countMapping;
        this.notifyDataSetChanged();
    }

}