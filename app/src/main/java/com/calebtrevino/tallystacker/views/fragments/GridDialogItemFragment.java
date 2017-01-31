package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
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
    private Game data;
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
            data = getArguments().getParcelable(GAME_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grd_view_dialog_detail_item, container, false);
        ButterKnife.bind(this, view);
        leagueName.setText(
                data.getLeagueType().getAcronym() + " - " + data.getLeagueType().getScoreType());
        dateTime.setText(
                DateTimeFormat.forPattern("MMM dd  hh:mm aa").print(new DateTime(data.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault())));

        firstTeamTitle.setText(
                data.getFirstTeam().getCity().trim());
        firstTeamSubtitle.setText(String.valueOf(
                data.getFirstTeam().get_teamID()));

        secondTeamTitle.setText(
                data.getSecondTeam().getCity());
        secondTeamSubtitle.setText(String.valueOf(
                data.getSecondTeam().get_teamID()));

        bidAmount.setText(getString(R.string.bid_amount,
                data.getLeagueType() instanceof Soccer_Spread ? "(" + (int) data.getVI_bid().getVigAmount() + ") " : data.getVI_bid().getCondition().getValue().replace("spread", ""),
                String.valueOf(data.getVI_bid().getBidAmount())));
        return view;
    }
}
