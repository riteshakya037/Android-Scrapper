package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.enums.BidResult;
import com.calebtrevino.tallystacker.presenters.GridViewPresenter;
import com.calebtrevino.tallystacker.utils.Constants;
import com.calebtrevino.tallystacker.views.fragments.GridViewDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final Context mContext;
    private final Grid mGrid;
    private List<Game> data;
    private GridViewPresenter viewPresenter;

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

    @SuppressWarnings("unused")
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
    public void onBindViewHolder(GridViewHolder holder, final int position) {
        if (position < data.size()) {
            final Game currentGame = data.get(position);
            holder.setBatchMarker(currentGame, position);
            holder.setOnClickListener(position);
            holder.setGridMarker(position);
            holder.leagueName.setText(data.get(position).getLeagueType().getAcronym());
            holder.teamsName.setText(mContext.getString(R.string.team_vs_team,
                    data.get(position).getFirstTeam().getName(),
                    data.get(position).getSecondTeam().getName()));
        } else {
            holder.leagueName.setText("");
            holder.teamsName.setText("");
            holder.gridMarker.setVisibility(View.GONE);
            holder.rootView.setOnClickListener(null);
        }
    }

    private void setBannerVisibility(long ts1, long ts2, View bannerView) {
        if (ts2 == 0) {
            bannerView.setVisibility(View.VISIBLE);
        } else {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(new DateTime(ts1, Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault()).toDate());
            cal2.setTime(new DateTime(ts2, Constants.DATE.VEGAS_TIME_ZONE).toDateTime(DateTimeZone.getDefault()).toDate());

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

            if (sameDay) {
                bannerView.setVisibility(View.GONE);
            } else {
                bannerView.setVisibility(View.VISIBLE);
            }

        }
    }

    public void setNullListener(GridViewPresenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    @SuppressWarnings("unused")
    public void addGames(List<Game> gameList) {
        data.addAll(gameList);
    }

    @SuppressWarnings("unused")
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
        @BindView(R.id.gridMarker)
        TextView gridMarker;
        @BindView(R.id.new_banner)
        View bannerView;
        @BindView(R.id.root_view)
        View rootView;

        GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setBatchMarker(Game currentGame, int position) {
            long previousTs = 0;
            if (position > 0) {
                previousTs = data.get(position - 1).getGameAddDate();
            }
            setBannerVisibility(currentGame.getGameAddDate(), previousTs, bannerView);
        }

        void setOnClickListener(final int position) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GridViewDialog.newInstance(new ArrayList<>(data), position).show(((AppCompatActivity) mContext).getSupportFragmentManager(), "");
                }
            });
        }

        void setGridMarker(int position) {
            if (data.get(position).getGameAddDate() == new DateTime(Constants.DATE.VEGAS_TIME_ZONE).withTimeAtStartOfDay().getMillis()) {
                BidResult previousStatus = BidResult.NEUTRAL;
                int count = 1;
                int modValue = position % mGrid.getRowNo();
                int column = 0;
                for (int i = 0; i < position; i++) {
                    if (i == column * mGrid.getRowNo() + modValue) {
                        if (previousStatus == data.get(i).getBidResult()) {
                            count++;
                        } else {
                            count = 1;
                            previousStatus = data.get(i).getBidResult();
                        }
                        column++;
                    }
                }
                gridMarker.setText(String.valueOf(count));
                switch (previousStatus) {
                    case NEUTRAL:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                        break;
                    case NEGATIVE:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorError));
                        break;
                    case DRAW:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDraw));
                        break;
                    case POSITIVE:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                        break;
                    default:
                        break;
                }
            } else {
                switch (data.get(position).getBidResult()) {
                    case NEUTRAL:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
                        break;
                    case NEGATIVE:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorError));
                        break;
                    case DRAW:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorDraw));
                        break;
                    case POSITIVE:
                        gridMarker.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                        break;
                    default:
                        break;
                }
                gridMarker.setText("");
            }
        }
    }
}
