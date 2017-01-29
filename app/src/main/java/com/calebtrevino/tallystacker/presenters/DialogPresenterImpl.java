package com.calebtrevino.tallystacker.presenters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.bases.League;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.GridLeagues;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.presenters.mapper.AddGridMapper;
import com.calebtrevino.tallystacker.views.DialogView;
import com.calebtrevino.tallystacker.views.adaptors.GridLeaguesAdaptor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */
public class DialogPresenterImpl implements DialogPresenter, GridLeaguesAdaptor.GridNameListener {
    private final DialogView mDialogView;
    private final AddGridMapper mGridMapper;
    private DatabaseContract.DbHelper dbHelper;
    private GridLeaguesAdaptor mGridLeaguesAdaptor;
    private List<League> leagues;

    public DialogPresenterImpl(DialogView dialogView, AddGridMapper gridMapper) {
        mDialogView = dialogView;
        mGridMapper = gridMapper;
    }

    @Override
    public void releaseAllResources() {
        if (mGridLeaguesAdaptor != null) {
            mGridLeaguesAdaptor = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void restorePosition() {
        if (mGridLeaguesAdaptor != null) {
            mGridLeaguesAdaptor = null;
        }
    }

    @Override
    public void initializeDataFromPreferenceSource() {
        mGridLeaguesAdaptor = new GridLeaguesAdaptor(mDialogView.getActivity(), this);
        mGridMapper.registerAdapter(mGridLeaguesAdaptor);
        leagues = dbHelper.getLeagues();
    }

    @Override
    public void initializeDatabase() {
        dbHelper = new DatabaseContract.DbHelper(mDialogView.getActivity());
    }

    @Override
    public void initializeViews() {
        mDialogView.initializeRecyclerLayoutManager(new LinearLayoutManager(mDialogView.getActivity()));

    }

    @Override
    public void saveState(Bundle outState) {

    }

    @Override
    public void restoreState(Bundle savedState) {

    }

    @Override
    public void createLeague() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mDialogView.getActivity());
        alertDialogBuilder.setTitle("Create League");
        LayoutInflater inflater = LayoutInflater.from(mDialogView.getActivity());

        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.add_league, null);

        final Spinner leagueSpinner = (Spinner) rootView.findViewById(R.id.leagueSpinner);

        final TextInputEditText startingNo = (TextInputEditText) rootView.findViewById(R.id.startingNo);
        final TextInputEditText endingNo = (TextInputEditText) rootView.findViewById(R.id.endingNo);

        List<String> list = new ArrayList<>();
        for (League league : leagues) {
            list.add(league.getAcronym() + " - " + league.getScoreType());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mDialogView.getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueSpinner.setAdapter(dataAdapter);
        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Game> copyGame = new LinkedList<>(mGridMapper.getGames());
                // Remove all the games for copyGame that aren't of the type currently selected.
                for (Game game : mGridMapper.getGames()) {
                    if (!game.getLeagueType().equals(leagues.get(position))) {
                        copyGame.remove(game);
                    }
                }
                startingNo.setText("1");
                endingNo.setText(copyGame.size() > 1 ? String.valueOf(copyGame.size()) : "1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialogBuilder.setView(rootView);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add League", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GridLeagues gridLeagues = DefaultFactory.GridLeagues.constructDefault();
                        gridLeagues.setLeague(leagues.get(leagueSpinner.getSelectedItemPosition()));
                        gridLeagues.setStartNo(Integer.parseInt(startingNo.getText().toString()));
                        gridLeagues.setEndNumber(Integer.parseInt(endingNo.getText().toString()));
                        gridLeagues.createID();
                        mGridLeaguesAdaptor.add(gridLeagues);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public Grid getGrid() {
        Grid grid = DefaultFactory.Grid.constructDefault();
        grid.setGridLeagues(mGridLeaguesAdaptor.getData());
        grid.setRowNo(Integer.parseInt(mGridMapper.getRowNo()));
        grid.setColumnNo(Integer.parseInt(mGridMapper.getColumnNo()));
        grid.setGridName(mGridMapper.getName());
        grid.createID();
        return grid;
    }

    @Override
    public void changeName(String gridName) {
        mGridMapper.setName(gridName);
    }
}
