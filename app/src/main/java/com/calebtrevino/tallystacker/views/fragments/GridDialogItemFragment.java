package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Spread;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.utils.Constants;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class GridDialogItemFragment extends Fragment {
    private static final String GAME_DATA = "game_data";
    private Game game;
    @BindView(R.id.leagueName)
    TextView leagueName;

    @BindView(R.id.dateTime)
    TextView dateTime;

    @BindView(R.id.firstTeamID)
    TextView firstTeamSubtitle;

    @BindView(R.id.firstTeamCity)
    TextView firstTeamTitle;

    @BindView(R.id.secondTeamID)
    TextView secondTeamSubtitle;

    @BindView(R.id.secondTeamCity)
    TextView secondTeamTitle;


    @BindView(R.id.bidAmount)
    TextView bidAmount;

    public static Fragment newInstance(Game game) {
        GridDialogItemFragment fragment = new GridDialogItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAME_DATA, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = getArguments().getParcelable(GAME_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grd_view_dialog_detail_item, container, false);
        ButterKnife.bind(this, view);
        leagueName.setText(
                game.getLeagueType().getAcronym() + " - " + game.getLeagueType().getScoreType());
        dateTime.setText(
                DateTimeFormat.forPattern("MMM dd  hh:mm aa").print(new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault())));

        firstTeamTitle.setText(
                game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        game.getFirstTeam().getCity() :
                        game.getFirstTeam().getName());
        firstTeamSubtitle.setText(
                game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        "-" :
                        game.getFirstTeam().getCity());
        secondTeamTitle.setText(
                game.getSecondTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        game.getSecondTeam().getCity() :
                        game.getSecondTeam().getName());
        secondTeamSubtitle.setText(
                game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        "-" :
                        game.getSecondTeam().getCity());
        bidAmount.setText(getString(R.string.bid_amount,
                game.getLeagueType() instanceof Soccer_Spread ? "(" + (int) game.getVI_bid().getVigAmount() + ") " : game.getVI_bid().getCondition().getValue().replace("spread", ""),
                String.valueOf(game.getVI_bid().getBidAmount())));
        return view;
    }
}
