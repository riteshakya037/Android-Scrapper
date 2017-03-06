package com.calebtrevino.tallystacker.views.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.utils.TeamPreference;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class LeagueViewAdaptor extends RecyclerView.Adapter<LeagueViewAdaptor.ViewHolder> {
    private static final int EVEN_TYPE = 0;
    private static final int ODD_TYPE = 1;
    private List<TeamPreference.TeamsWrapper> data;

    public LeagueViewAdaptor() {
        data = new LinkedList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        int resId;
        switch (viewType) {
            case 1:
                resId = R.layout.league_item_right;
                break;
            case 0:
            default:
                resId = R.layout.league_item_left;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(
                resId, parent, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setInfo(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return EVEN_TYPE;
        } else
            return ODD_TYPE;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TeamPreference.TeamsWrapper> data) {
        this.data = data;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.league_item_team_abbr)
        TextView teamAbbr;
        @BindView(R.id.league_item_team_name)
        TextView teamName;
        @BindView(R.id.league_item_team_city)
        TextView teamCity;
        @BindView(R.id.league_item_vegas_display)
        TextView teamVegas;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setInfo(TeamPreference.TeamsWrapper teamsWrapper) {
            teamAbbr.setText(teamsWrapper.getTeamAbbr());
            teamName.setText(teamsWrapper.getTeamName());
            teamCity.setText(teamsWrapper.getTeamCity());
            teamVegas.setText(teamsWrapper.getVegasDisplay());
        }
    }
}