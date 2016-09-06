package com.calebtrevino.tallystacker.presenters.bases;

import android.support.v7.widget.RecyclerView;

/**
 * Created by fatal on 9/6/2016.
 */
public interface BaseRecycleAdapterMapper {
    public void registerAdapter(RecyclerView.Adapter<?> adapter);
}
