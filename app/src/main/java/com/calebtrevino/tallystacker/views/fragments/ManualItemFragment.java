package com.calebtrevino.tallystacker.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.CalculateResult;
import com.calebtrevino.tallystacker.controllers.sources.espn_scrappers.exceptions.InvalidScoreTypeException;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.IntermediateResult;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.utils.StringUtils;

/**
 * @author Ritesh Shakya
 */
public class ManualItemFragment extends Fragment {
    private static final String GAME_DATA = "game_data";
    @BindView(R.id.leagueName) protected TextView leagueName;
    @BindView(R.id.secondTeamCity) protected TextView secondTeamTitle;
    @BindView(R.id.firstTeamCity) protected TextView firstTeamTitle;
    @BindView(R.id.firstTeamScore) protected EditText firstTeamScore;
    @BindView(R.id.secondTeamScore) protected EditText secondTeamScore;
    private Game game;

    public static ManualItemFragment newInstance(Game game) {
        ManualItemFragment fragment = new ManualItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAME_DATA, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = getArguments().getParcelable(GAME_DATA);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manual_entry_item, container, false);
        ButterKnife.bind(this, view);
        leagueName.setText(
                game.getLeagueType().getAcronym() + " - " + game.getLeagueType().getScoreType());
        firstTeamTitle.setText(game.getFirstTeam().getCity());
        secondTeamTitle.setText(game.getSecondTeam().getCity());
        return view;
    }

    public void saveGame() throws InvalidScoreTypeException {
        if (StringUtils.isNotNull(firstTeamScore.getText().toString()) && StringUtils.isNotNull(
                secondTeamScore.getText().toString())) {
            game.getFirstTeam().setAcronym(game.getFirstTeam().getCity());
            game.getSecondTeam().setAcronym(game.getSecondTeam().getCity());
            IntermediateResult result = new IntermediateResult();
            result.setGameStatus(GameStatus.COMPLETE);
            result.add(game.getFirstTeam().getCity(), firstTeamScore.getText().toString());
            result.add(game.getSecondTeam().getCity(), secondTeamScore.getText().toString());
            CalculateResult.ResultOut resultOut =
                    (new CalculateResult()).calculateResult(game, result);
            CalculateResult.setResult(game, result, resultOut, resultOut.getGameStatus());
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(getActivity());
            dbHelper.onUpdateGame(game.getId(), game);
            dbHelper.close();
            firstTeamScore.setText("");
            secondTeamScore.setText("");
        } else {
            if (StringUtils.isNotNull(firstTeamScore.getText().toString())) {
                firstTeamScore.setError("Null");
            }
            if (StringUtils.isNotNull(secondTeamScore.getText().toString())) {
                secondTeamScore.setError("Null");
            }
        }
    }
}