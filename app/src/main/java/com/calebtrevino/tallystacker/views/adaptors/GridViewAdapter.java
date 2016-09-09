package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.GridViewPresenter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fatal on 9/7/2016.
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private Grid mCurrentGrid;
    private Context mContext;

    List<Game> data;
    private GridViewPresenter viewPresenter;

    public GridViewAdapter(Context context) {
        this.mContext = context;
        data = new LinkedList<>();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grd_view_item, parent, false);
        viewHolder = new GridViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return 15 * 20;
    }

    public void addGame(Game game) {
        if (!data.contains(game)) {
            data.add(game);
        }
        if (data.size() > 0) {
            viewPresenter.isEmpty(false);  // Broadcast that dataset is not empty.
        }
        this.notifyDataSetChanged();
    }


    public void removeCard(Game game) {
        data.remove(game);
        if (data.size() == 0) {
            viewPresenter.isEmpty(true); // Broadcast that dataset is empty.
        }
        this.notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        if (position < data.size()) {
            holder.leagueName.setText(data.get(position).getLeagueType().getAcronym());
            holder.teamsName.setText(mContext.getString(R.string.team_vs_team,
                    data.get(position).getFirstTeam().getCity().substring(1, 5),
                    data.get(position).getSecondTeam().getCity().substring(1, 5)));
        } else {
            holder.leagueName.setText("");
            holder.teamsName.setText("");
        }
    }

    public void setNullListener(GridViewPresenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.leagueName)
        TextView leagueName;
        @BindView(R.id.teamsName)
        TextView teamsName;

        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
