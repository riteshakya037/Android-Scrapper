package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.sources.Soccer_Spread;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.presenters.DashPresenter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class DashAdapter extends RecyclerView.Adapter<DashAdapter.DashViewHolder> {
    private final List<Game> data;
    private DashPresenter dashPresenter;
    private final Context mContext;

    public DashAdapter(Context context) {
        mContext = context;
        data = new LinkedList<>();
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
        holder.leagueName.setText(
                data.get(position).getLeagueType().getAcronym() + " - " + data.get(position).getLeagueType().getScoreType());
        holder.dateTime.setText(mContext.getString(R.string.schedule,
                new SimpleDateFormat("MMM dd  hh:mm aa", Locale.getDefault()).format(
                        new Date(data.get(position).getGameDateTime()))));
        holder.firstTeamID.setText(String.valueOf(
                data.get(position).getFirstTeam().get_teamID()));

        holder.firstTeamCity.setText(
                data.get(position).getFirstTeam().getCity().trim());
        holder.secondTeamID.setText(String.valueOf(
                data.get(position).getSecondTeam().get_teamID()));
        holder.secondTeamCity.setText(
                data.get(position).getSecondTeam().getCity());
        holder.bidAmount.setText(mContext.getString(R.string.bid_amount,
                data.get(position).getLeagueType() instanceof Soccer_Spread ? "(" + (int) data.get(position).getVI_bid().getVigAmount() + ") " : data.get(position).getVI_bid().getCondition().getValue().replace("spread", ""),
                String.valueOf(data.get(position).getVI_bid().getBidAmount())));
    }

    public void addGame(Game game) {
        if (!data.contains(game)) {
            data.add(game);
            Collections.sort(data, new Game.GameComparator());
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

    public List<Game> getGames() {
        return data;
    }


    public class DashViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leagueName)
        TextView leagueName;

        @BindView(R.id.dateTime)
        TextView dateTime;

        @BindView(R.id.firstTeamID)
        TextView firstTeamID;

        @BindView(R.id.firstTeamCity)
        TextView firstTeamCity;

        @BindView(R.id.secondTeamID)
        TextView secondTeamID;

        @BindView(R.id.secondTeamCity)
        TextView secondTeamCity;


        @BindView(R.id.bidAmount)
        TextView bidAmount;

        public DashViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
