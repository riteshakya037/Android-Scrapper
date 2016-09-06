package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calebtrevino.tallystacker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridViewFragment extends GridHolderFragment {

    @BindView(R.id.gridViewRecycler)
    RecyclerView mGridViewRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_view, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
