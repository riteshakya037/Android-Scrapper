package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.models.GridLeagues;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fatal on 9/9/2016.
 */
public class GridLeaguesAdaptor extends RecyclerView.Adapter<GridLeaguesAdaptor.GridLeaguesHolder> {
    List<GridLeagues> gridLeaguesList;
    private Context mContext;

    public GridLeaguesAdaptor(Context context) {
        mContext = context;
        gridLeaguesList = new LinkedList<>();
        gridLeaguesList.add(DefaultFactory.GridLeagues.constructDefault());
        gridLeaguesList.add(DefaultFactory.GridLeagues.constructDefault());
        gridLeaguesList.add(DefaultFactory.GridLeagues.constructDefault());
    }

    @Override
    public GridLeaguesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridLeaguesHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.grd_view_item, parent, false);
        viewHolder = new GridLeaguesHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridLeaguesHolder holder, final int position) {
        holder.leagueName.setText(gridLeaguesList.get(position).getLeague().getName());
        holder.leaguePosition.setText(mContext.getString(R.string.range_1_2,
                String.valueOf(gridLeaguesList.get(position).getStartNo()),
                String.valueOf(gridLeaguesList.get(position).getEndNumber())));
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridLeaguesList.remove(position);
                GridLeaguesAdaptor.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return gridLeaguesList.size();
    }

    public int getLastLeaguesEnd() {
        if (gridLeaguesList.size() == 0) {
            return 1;
        } else {
            return gridLeaguesList.get(gridLeaguesList.size() - 1).getEndNumber() + 1;
        }
    }

    public void add(GridLeagues gridLeagues) {
        gridLeaguesList.add(gridLeagues);
        this.notifyDataSetChanged();
    }

    public List<GridLeagues> getData() {
        return gridLeaguesList;
    }

    public class GridLeaguesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leagueName)
        TextView leagueName;

        @BindView(R.id.leaguePosition)
        TextView leaguePosition;

        @BindView(R.id.removeItem)
        ImageButton removeItem;


        public GridLeaguesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
