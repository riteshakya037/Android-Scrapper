package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Spread;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
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
    private Game game;

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
        switch (game.getBidResult()) {
            case NEUTRAL:
                leagueName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                break;
            case NEGATIVE:
                leagueName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorError));
                break;
            case DRAW:
                leagueName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDraw));
                break;
            case POSITIVE:
                leagueName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                break;
        }
        if (game.getGameStatus() == GameStatus.CANCELLED) {
            leagueName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDraw));
        }
        dateTime.setText(
                DateTimeFormat.forPattern("MMM dd  hh:mm aa").print(new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault())));

        firstTeamTitle.setText(
                game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        game.getFirstTeam().getCity() :
                        game.getFirstTeam().getName() + " " + String.valueOf(game.getFirstTeamScore()));
        firstTeamSubtitle.setText(
                game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        "-" :
                        game.getFirstTeam().getCity());
        secondTeamTitle.setText(
                game.getSecondTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        game.getSecondTeam().getCity() :
                        game.getSecondTeam().getName() + " " + String.valueOf(game.getSecondTeamScore()));
        secondTeamSubtitle.setText(
                game.getSecondTeam().getName().equals(DefaultFactory.Team.NAME) ?
                        "-" :
                        game.getSecondTeam().getCity());
        bidAmount.setText(getContext().getString(R.string.bid_amount,
                game.getLeagueType() instanceof Soccer_Spread ? "(" + (int) game.getVIBid().getVigAmount() + ") " : game.getVIBid().getCondition().getValue().replace("spread", ""),
                String.valueOf(game.getVIBid().getBidAmount())));
        return view;
    }
}
