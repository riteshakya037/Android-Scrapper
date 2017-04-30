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
import com.calebtrevino.tallystacker.presenters.DashPresenter;
import com.calebtrevino.tallystacker.presenters.events.DashCountEvent;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class DashAdapter extends RecyclerView.Adapter<DashAdapter.DashViewHolder> {
    private final List<Game> data;
    private final Context mContext;
    private DashPresenter dashPresenter;
    private Comparator<Game> comparator;

    public DashAdapter(Context context) {
        mContext = context;
        data = new LinkedList<>();
        comparator = new Game.GameTimeComparator();
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

    public void addGame(Game game) {
        if (!data.contains(game)) {
            data.add(game);
            Collections.sort(data, comparator);
            EventBus.getDefault().post(new DashCountEvent(data.size()));
        }
        if (data.size() > 0) {
            dashPresenter.isEmpty(false);  // Broadcast that dataset is not empty.
        }
        this.notifyDataSetChanged();
    }


    public void removeCard(Game game) {
        data.remove(game);
        if (data.size() == 0) {
            dashPresenter.isEmpty(true); // Broadcast that dataset is empty.
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setNullListener(DashPresenter dashPresenter) {
        this.dashPresenter = dashPresenter;
    }


    public void changeSort(Comparator<Game> comparator) {
        this.comparator = comparator;
        Collections.sort(data, comparator);
        this.notifyDataSetChanged();
    }

    public void modify(Game game) {
        if (data.contains(game)) {
            data.set(data.indexOf(game), game);
            this.notifyDataSetChanged();
        }
    }

    class DashViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.gameFound)
        ImageView gameFound;

        @BindView(R.id.bidAmount)
        TextView bidAmount;

        DashViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setInfo(Game game) {
            leagueName.setText(
                    game.getLeagueType().getAcronym() + " - " + game.getLeagueType().getScoreType());
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
            }
            if (game.getGameStatus() == GameStatus.CANCELLED) {
                leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorDraw));
            }
            gameFound.setVisibility(StringUtils.isNull(game.getGameUrl()) ? View.GONE : View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!DatabaseContract.DbHelper.checkBidValid(game)) {
                    gameFound.setImageTintList(ContextCompat.getColorStateList(mContext, android.R.color.black));
                } else {
                    gameFound.setImageTintList(ContextCompat.getColorStateList(mContext, R.color.colorAccent));
                }
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
            bidAmount.setText(mContext.getString(R.string.bid_amount,
                    game.getLeagueType() instanceof Soccer_Spread ? "(" + (int) game.getVI_bid().getVigAmount() + ") " : game.getVI_bid().getCondition().getValue().replace("spread", ""),
                    String.valueOf(game.getVI_bid().getBidAmount())));
        }
    }
}
