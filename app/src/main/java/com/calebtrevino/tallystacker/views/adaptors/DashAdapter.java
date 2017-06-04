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
    private int dateLag;
    private DashPagerPresenter dashPresenter;
    private Comparator<Game> comparator;

    public DashAdapter(Context context, int dateLag) {
        mContext = context;
        this.dateLag = dateLag;
        data = new LinkedList<>();
        comparator = new Game.VIComparator();
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
            setGroupStatus();
            EventBus.getDefault().post(new DashCountEvent(data.size(), dateLag));
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

    public void setNullListener(DashPagerPresenter dashPresenter) {
        this.dashPresenter = dashPresenter;
    }


    public void changeSort(Comparator<Game> comparator) {
        this.comparator = comparator;
        Collections.sort(data, comparator);
        setGroupStatus();
        this.notifyDataSetChanged();
    }

    public void modify(Game game) {
        if (data.contains(game)) {
            data.set(data.indexOf(game), game);
            this.notifyDataSetChanged();
        }
    }

    private void setGroupStatus() {
        int groupNo = 1;
        if (comparator instanceof Game.GameTimeComparator) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).resetGroup();
                if (i != 0 && data.get(i).getGameDateTime() != data.get(i - 1).getGameDateTime()) {
                    groupNo++;
                }
                data.get(i).setGroup(groupNo);
            }
        } else if (comparator instanceof Game.VIComparator) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).resetGroup();
                if (i != 0 && !data.get(i).getLeagueType().getPackageName().equals(data.get(i - 1).getLeagueType().getPackageName())) {
                    groupNo = 1;
                }
                if (DatabaseContract.DbHelper.checkBidValid(data.get(i))) {
                    data.get(i).setGroup(groupNo++);
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).resetGroup();
            }
        }
    }

    class DashViewHolder extends RecyclerView.ViewHolder {
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
                default:
                    break;
            }
            if (game.getGameStatus() == GameStatus.CANCELLED) {
                leagueName.setTextColor(ContextCompat.getColor(mContext, R.color.colorDraw));
            }
            gameFound.setVisibility(StringUtils.isNull(game.getGameUrl()) ? View.INVISIBLE : View.VISIBLE);
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
                    game.getLeagueType() instanceof Soccer_Spread ? "(" + (int) game.getVIBid().getVigAmount() + ") " : game.getVIBid().getCondition().getValue().replace("spread", ""),
                    String.valueOf(game.getVIBid().getBidAmount())));
            if (game.getGroup() != -1) {
                numberText.setText(String.valueOf(game.getGroup()));
            } else {
                numberText.setText("");
            }
        }
    }
}
