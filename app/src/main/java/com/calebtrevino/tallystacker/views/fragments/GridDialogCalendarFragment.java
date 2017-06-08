package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class GridDialogCalendarFragment extends Fragment {
    private static final String GAME_DATA = "game_data";
    @BindView(R.id.leagueName)
    protected TextView leagueName;
    @BindView(R.id.dateTime)
    protected TextView dateTime;
    @BindView(R.id.firstTeamID)
    protected TextView firstTeamSubtitle;
    @BindView(R.id.firstTeamCity)
    protected TextView firstTeamTitle;
    @BindView(R.id.secondTeamID)
    protected TextView secondTeamSubtitle;
    @BindView(R.id.secondTeamCity)
    protected TextView secondTeamTitle;
    @BindView(R.id.bidAmount)
    protected TextView bidAmount;
    private ArrayList<Game> games;

    public static Fragment newInstance(ArrayList<Game> games) {
        GridDialogCalendarFragment fragment = new GridDialogCalendarFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(GAME_DATA, games);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            games = getArguments().getParcelableArrayList(GAME_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grd_calendar_dialog_detail_item, container, false);
        ButterKnife.bind(this, view);
        System.out.println("games = " + games);
        return view;
    }
}
