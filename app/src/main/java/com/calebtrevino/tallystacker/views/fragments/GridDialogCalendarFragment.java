package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.views.adaptors.GridCalendarRecyclerAdapter;
import java.util.ArrayList;

/**
 * @author Ritesh Shakya
 */
public class GridDialogCalendarFragment extends Fragment {
    private static final String GAME_DATA = "game_data";

    @BindView(R.id.activity_dialog_calendar_recycler) RecyclerView recyclerView;
    private ArrayList<Game> games;

    public static Fragment newInstance(ArrayList<Game> games) {
        GridDialogCalendarFragment fragment = new GridDialogCalendarFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(GAME_DATA, games);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            games = getArguments().getParcelableArrayList(GAME_DATA);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grd_calendar_dialog_detail_item, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GridCalendarRecyclerAdapter adapter = new GridCalendarRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setGames(games);
    }
}
