package com.calebtrevino.tallystacker.views.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.views.fragments.LeaguePageFragment;
import java.util.List;

/**
 * @author Ritesh Shakya
 */

public class LeagueFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<League> leagueList;

    public LeagueFragmentPagerAdapter(FragmentManager fm, List<League> leagueList) {
        super(fm);
        this.leagueList = leagueList;
    }

    @Override public Fragment getItem(int position) {
        return LeaguePageFragment.newInstance(leagueList.get(position));
    }

    @Override public int getCount() {
        return leagueList.size();
    }

    @Override public CharSequence getPageTitle(int position) {
        return leagueList.get(position).getAcronym() + " - " + leagueList.get(position)
                .getScoreType();
    }
}