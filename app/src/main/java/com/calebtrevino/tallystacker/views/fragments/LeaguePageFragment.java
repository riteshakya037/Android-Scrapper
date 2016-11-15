package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.bases.League;

import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class LeaguePageFragment extends Fragment {
    private static final String ARG_LEAGUE = "league";
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private League league;

    public static Fragment newInstance(League league) {
        LeaguePageFragment fragment = new LeaguePageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LEAGUE, league);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            league = getArguments().getParcelable(ARG_LEAGUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.league_single_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
