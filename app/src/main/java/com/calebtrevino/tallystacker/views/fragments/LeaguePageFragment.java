package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.utils.TeamPreference;
import com.calebtrevino.tallystacker.views.adaptors.LeagueViewAdaptor;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class LeaguePageFragment extends Fragment {
    private static final String ARG_LEAGUE = "league";
    @BindView(R.id.leagueViewRecycler)
    protected RecyclerView mRecyclerView;
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
        LeagueViewAdaptor adapter = new LeagueViewAdaptor();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            adapter.setData(TeamPreference.getInstance(getContext(), league).getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
