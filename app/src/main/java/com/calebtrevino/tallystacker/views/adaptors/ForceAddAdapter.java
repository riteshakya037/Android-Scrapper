package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.models.GridLeagues;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Ritesh Shakya
 */
public class ForceAddAdapter extends RecyclerView.Adapter<ForceAddAdapter.ForceAddHolder> {
    private final Context mContext;
    private final List<GridLeagues> data;
    private final ClickListener mListener;

    public ForceAddAdapter(Context context, Grid grid, ClickListener listener) {
        mContext = context;
        data = grid.getGridLeagues();
        mListener = listener;
    }

    @Override
    public ForceAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ForceAddHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.force_add_item, parent, false);
        viewHolder = new ForceAddHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForceAddHolder holder, int position) {
        final int mPosition = position;
        holder.forceSwitch.setChecked(data.get(position).isForceAdd());
        holder.forceSwitch.setText(mContext.getString(R.string.league_start_end, data.get(position).getLeague().getAcronym(), String.valueOf(data.get(position).getStartNo()), String.valueOf(data.get(position).getEndNumber())));
        holder.forceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(mPosition).setForceAdd(isChecked);
                mListener.onForceAddClick(data);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ForceAddHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.forceSwitch)
        Switch forceSwitch;

        public ForceAddHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onForceAddClick(List<GridLeagues> gridLeagues);
    }
}
