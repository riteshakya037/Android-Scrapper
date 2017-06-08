package com.calebtrevino.tallystacker.views.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.views.adaptors.GridDialogCalendarAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class GridCalendarActivity extends AppCompatActivity {

    public static final String DATA_MAP = "data_map";
    public static final String POSITION = "current_date";
    public static final String COUNT_MAP = "count_map";
    @BindView(R.id.container)
    protected ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    protected TabLayout mTabLayout;
    private HashMap<Long, ArrayList<Game>> data;
    private HashMap<Integer, Long> countMapping;
    private long currentDate;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_calendar);
        this.currentDate = getIntent().getLongExtra(POSITION, 0);
        this.data = (HashMap<Long, ArrayList<Game>>) getIntent().getSerializableExtra(DATA_MAP);
        this.countMapping = (HashMap<Integer, Long>) getIntent().getSerializableExtra(COUNT_MAP);

        ButterKnife.bind(this);
        GridDialogCalendarAdapter adapter = new GridDialogCalendarAdapter(getSupportFragmentManager(), this);
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
    }
}
