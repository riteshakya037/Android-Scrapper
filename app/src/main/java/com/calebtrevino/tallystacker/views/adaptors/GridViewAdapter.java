package com.calebtrevino.tallystacker.views.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
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

    List<String> data;
    private GridViewPresenter viewPresenter;

    public GridViewAdapter(Context context, Grid currentGrid) {
        this.mCurrentGrid = currentGrid;
        this.mContext = context;
        data = new LinkedList<>();
        int[] array = new int[100];
        for (int a = 0; a < array.length; a++) {
            data.add(String.valueOf(a));
        }
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
        if (data.size() == 0) {
            viewPresenter.isEmpty(true);
        } else {
            viewPresenter.isEmpty(false);
        }
        return data.size();
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.text_view.setText(data.get(position));
    }

    public void setNullListener(GridViewPresenter viewPresenter) {
        this.viewPresenter = viewPresenter;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view)
        TextView text_view;

        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
