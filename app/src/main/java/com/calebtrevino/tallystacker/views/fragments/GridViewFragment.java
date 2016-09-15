package com.calebtrevino.tallystacker.views.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.GridViewPresenter;
import com.calebtrevino.tallystacker.presenters.GridViewPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.GridViewMapper;
import com.calebtrevino.tallystacker.views.GridViewView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridViewFragment extends GridHolderFragment implements GridViewView, GridViewMapper {

    @BindView(R.id.gridViewRecycler)
    RecyclerView mGridViewRecycler;


    @BindView(R.id.emptyRelativeLayout)
    RelativeLayout mEmptyRelativeLayout;

    GridViewPresenter mGridViewPresenter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GridHolderFragment newInstance() {
        return new GridViewFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGridViewPresenter = new GridViewPresenterImpl(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_view, container, false);
        ButterKnife.bind(this, rootView);
        mGridViewRecycler.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGridViewPresenter.initializeViews();
        if (savedInstanceState != null) {
            mGridViewPresenter.restoreState(savedInstanceState);
        }

        mGridViewPresenter.initializeDatabase();
//        mGridViewPresenter.initializeDataFromPreferenceSource();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGridViewPresenter.releaseAllResources();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridViewPresenter.saveState(outState);
    }


    @Override
    public Parcelable getPositionState() {
        if (mGridViewRecycler != null) {
            return mGridViewRecycler.getLayoutManager().onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override
    public void setPositionState(Parcelable state) {
        if (mGridViewRecycler != null) {
            mGridViewRecycler.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    public void initializeEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            ((ImageView) mEmptyRelativeLayout.findViewById(R.id.emptyImageView)).setImageResource(R.drawable.empty_grid);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.emptyTextView)).setText(null);
            ((TextView) mEmptyRelativeLayout.findViewById(R.id.instructionsTextView)).setText(R.string.loading);
        }
    }

    @Override
    public void hideEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyRelativeLayout() {
        if (mEmptyRelativeLayout != null) {
            mEmptyRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_grid);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }

    @Override
    public void registerAdapter(final RecyclerView.Adapter<?> adapter) {
        if (mGridViewRecycler != null) {
            mGridViewRecycler.setAdapter(adapter);
        }
    }

    @Override
    public void resetScale() {
        if (mGridViewRecycler != null) {
            mGridViewRecycler.setScaleX(1);
            mGridViewRecycler.setScaleY(1);
        }
    }

    @Override
    public void initializeBasePageView() {
        if (mGridViewRecycler != null) {
//            mDashRecycler.addOnItemTouchListener(); // TODO: 9/6/2016
        }
    }

    @Override
    public void initializeRecyclerLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mGridViewRecycler != null) {
            mGridViewRecycler.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void added(Grid grid) {
        mGridViewPresenter.changeGrid(grid);
    }

}
