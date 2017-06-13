package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.controllers.sources.vegas_scrappers.Soccer_Spread;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.database.DatabaseContract;
import com.calebtrevino.tallystacker.models.enums.GameStatus;
import com.calebtrevino.tallystacker.presenters.DashPagerPresenter;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.utils.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class GridCalendarRecyclerAdapter extends RecyclerView.Adapter<GridCalendarRecyclerAdapter.DashViewHolder> {
    private final Context mContext;
    private ArrayList<Game> data;
    private DashPagerPresenter dashPresenter;
    private Comparator<Game> comparator;

    public GridCalendarRecyclerAdapter(Context context) {
        mContext = context;
        data = new ArrayList<>();
    }

    @Override
    public DashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DashViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dash_item, parent, false);
        viewHolder = new DashViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DashViewHolder holder, int position) {
        holder.setInfo(data.get(position));
    }

    public void setGames(ArrayList<Game> games) {
        data = games;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class DashViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.numberText)
        protected TextView numberText;

        @BindView(R.id.gameFound)
        protected ImageView gameFound;

        @BindView(R.id.bidAmount)
        protected TextView bidAmount;

        private DashViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setInfo(Game game) {
            setColors(game);
            setTexts(game);
        }

        private void setTexts(Game game) {
            leagueName.setText(
                    game.getLeagueType().getAcronym() + " - " + game.getLeagueType().getScoreType());
            gameFound.setVisibility(StringUtils.isNull(game.getGameUrl()) ? View.INVISIBLE : View.VISIBLE);

            dateTime.setText(
                    DateTimeFormat.forPattern("MMM dd  hh:mm aa").print(new DateTime(game.getGameDateTime(), Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault())));

            if (game.getFirstTeam().getName().equals(DefaultFactory.Team.NAME)) {
                firstTeamTitle.setText(game.getFirstTeam().getCity());
                firstTeamSubtitle.setText("-");
            } else {
                firstTeamTitle.setText(game.getFirstTeam().getName() + " " + String.valueOf(game.getFirstTeamScore()));
                firstTeamSubtitle.setText(game.getFirstTeam().getCity());
            }
            if (game.getSecondTeam().getName().equals(DefaultFactory.Team.NAME)) {
                secondTeamTitle.setText(game.getSecondTeam().getCity());
                secondTeamSubtitle.setText("-");
            } else {
                secondTeamTitle.setText(game.getSecondTeam().getName() + " " + String.valueOf(game.getSecondTeamScore()));
                secondTeamSubtitle.setText(game.getSecondTeam().getCity());
            }
            bidAmount.setText(mContext.getString(R.string.bid_amount,
                    game.getLeagueType() instanceof Soccer_Spread ? "(" + (int) game.getVIBid().getVigAmount() + ") " : game.getVIBid().getCondition().getValue().replace("spread", ""),
                    String.valueOf(game.getVIBid().getBidAmount())));
            if (game.getGroup() != -1) {
                numberText.setText(String.valueOf(game.getGroup()));
            } else {
                numberText.setText("");
            }
        }

        private void setColors(Game game) {
            switch (game.getBidResult()) {
                case NEUTRAL:
                    leagueName.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                    break;
                case NEGATIVE:
                    leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorError));
                    break;
                case DRAW:
                    leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorDraw));
                    break;
                case POSITIVE:
                    leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                    break;
                default:
                    break;
            }
            if (game.getGameStatus() == GameStatus.CANCELLED) {
                leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorDraw));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!DatabaseContract.DbHelper.checkBidValid(game)) {
                    gameFound.setImageTintList(ContextCompat.getColorStateList(mContext, android.R.color.black));
                } else {
                    gameFound.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.colorAccent));
                }
            }
        }
    }
}
