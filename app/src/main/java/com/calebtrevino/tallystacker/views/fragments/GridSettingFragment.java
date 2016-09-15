package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;

import butterknife.ButterKnife;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridSettingFragment extends GridHolderFragment {

    public static GridHolderFragment newInstance() {
        return new GridSettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_setting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void added(Grid grid) {

    }

}
