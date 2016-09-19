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
 * @author Ritesh Shakya
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final Context mContext;

    private List<Game> data;
    private Grid mGrid;
    private GridViewPresenter viewPresenter;

    public GridViewAdapter(Context context) {
        this.mContext = context;
        data = new LinkedList<>();
    }

    public GridViewAdapter(Context context, Grid grid) {
        this.mContext = context;
        data = grid.getGameList();
        mGrid = grid;
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
        return mGrid.getRowNo() * mGrid.getColumnNo();
    }

    public void addGames(Game game) {
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
                    data.get(position).getFirstTeam().getCity().substring(0, 4),
                    data.get(position).getSecondTeam().getCity().substring(0, 4)));
        } else {
            holder.leagueName.setText("");
            holder.teamsName.setText("");
        }
    }

    public void setNullListener(GridViewPresenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    public void addGames(List<Game> gameList) {
        data.addAll(gameList);
    }

    public void setData(List<Game> gameList) {
        data = new LinkedList<>();
        data.addAll(gameList);
        this.notifyDataSetChanged();
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
