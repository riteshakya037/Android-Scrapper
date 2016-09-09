package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.presenters.DashPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fatal on 9/9/2016.
 */
public class DashAdapter extends RecyclerView.Adapter<DashAdapter.DashViewHolder> {
    private List<Game> data;
    private DashPresenter dashPresenter;
    private Context mContext;

    public DashAdapter(Context context) {
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
        holder.leagueName.setText(
                data.get(position).getLeagueType().getName());
        holder.dateTime.setText(mContext.getString(R.string.schedule,
                new SimpleDateFormat("EEE MMM dd  hh:mm", Locale.getDefault()).format(
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
                data.get(position).getBidList().get(0).getCondition().getValue(),
                String.valueOf(data.get(position).getBidList().get(0).getBidAmount())));
    }

    public void addGame(Game game) {
        if (!data.contains(game)) {
            data.add(game);
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
