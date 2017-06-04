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
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.views.fragments.GridDialogItemFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */

public class GridDialogPagerAdapter extends FragmentStatePagerAdapter {
    @BindView(R.id.leagueName)
    protected TextView leagueName;
    @BindView(R.id.teamsName)
    protected TextView teamsName;
    @BindView(R.id.new_banner)
    protected View bannerView;

    private final Context mContext;
    private List<Game> mData;

    public GridDialogPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        this.mData = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return GridDialogItemFragment.newInstance(mData.get(position));
    }

    @Override
    public int getCount() {
        return mData.size();
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.grd_view_dialog_item, null);
        ButterKnife.bind(this, view);
        final Game currentGame = mData.get(position);
        long previousTs = 0;
        if (position > 0) {
            previousTs = mData.get(position - 1).getGameAddDate();
        }
        setBannerVisibility(currentGame.getGameAddDate(), previousTs, bannerView);
        leagueName.setText(mData.get(position).getLeagueType().getAcronym());
        teamsName.setText(mContext.getString(R.string.team_vs_team,
                mData.get(position).getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ? mData.get(position).getFirstTeam().getCity() : mData.get(position).getFirstTeam().getName(),
                mData.get(position).getSecondTeam().getName().equals(DefaultFactory.Team.NAME) ? mData.get(position).getSecondTeam().getCity() : mData.get(position).getSecondTeam().getName()));
        return view;
    }

    public void setData(List<Game> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    private void setBannerVisibility(long ts1, long ts2, View bannerView) {
        if (ts2 == 0) {
            bannerView.setVisibility(View.VISIBLE);
        } else {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(new DateTime(ts1, Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault()).toDate());
            cal2.setTime(new DateTime(ts2, Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault()).toDate());

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

            if (sameDay) {
                bannerView.setVisibility(View.GONE);
            } else {
                bannerView.setVisibility(View.VISIBLE);
            }

        }
    }
}