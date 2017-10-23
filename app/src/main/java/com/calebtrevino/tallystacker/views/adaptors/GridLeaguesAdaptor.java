package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.GridLeagues;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ritesh Shakya
 */
public class GridLeaguesAdaptor extends RecyclerView.Adapter<GridLeaguesAdaptor.GridLeaguesHolder> {
    private final List<GridLeagues> gridLeaguesList;
    private final Context mContext;
    private final GridNameListener mListener;

    public GridLeaguesAdaptor(Context context, GridNameListener listener) {
        mContext = context;
        mListener = listener;
        gridLeaguesList = new LinkedList<>();
    }

    @Override public GridLeaguesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridLeaguesHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_league_item, parent, false);
        viewHolder = new GridLeaguesHolder(v);
        return viewHolder;
    }

    @Override public void onBindViewHolder(GridLeaguesHolder holder, int position) {
        final int mPosition = position;
        holder.leagueName.setText(gridLeaguesList.get(position).getLeague().getName());
        holder.leaguePosition.setText(mContext.getString(R.string.range_1_2,
                String.valueOf(gridLeaguesList.get(position).getStartNo()),
                String.valueOf(gridLeaguesList.get(position).getEndNumber())));
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                gridLeaguesList.remove(mPosition);
                GridLeaguesAdaptor.this.notifyDataSetChanged();
                changeName();
            }
        });
    }

    private void changeName() {
        String gridName = "";
        for (GridLeagues gridLeagues : gridLeaguesList) {
            gridName += gridLeagues.getLeague().getAcronym()
                    + "-"
                    + gridLeagues.getLeague()
                    .getScoreType()
                    + "-"
                    + gridLeagues.getStartNo()
                    + "-"
                    + gridLeagues.getEndNumber();
        }
        mListener.changeName(gridName);
    }

    @Override public int getItemCount() {
        return gridLeaguesList.size();
    }

    public void add(GridLeagues gridLeagues) {
        gridLeaguesList.add(gridLeagues);
        this.notifyDataSetChanged();
        changeName();
    }

    public List<GridLeagues> getData() {
        return gridLeaguesList;
    }

    public interface GridNameListener {
        void changeName(String gridName);
    }

    public class GridLeaguesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leagueName) protected TextView leagueName;

        @BindView(R.id.leaguePosition) protected TextView leaguePosition;

        @BindView(R.id.removeItem) protected ImageButton removeItem;

        private GridLeaguesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
