package com.calebtrevino.tallystacker.presenters.bases;

import android.support.v7.widget.RecyclerView;

/**
 * @author Ritesh Shakya
 */
public interface BaseRecycleAdapterMapper {
    void registerAdapter(RecyclerView.Adapter<?> adapter);
}
