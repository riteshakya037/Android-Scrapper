package com.calebtrevino.tallystacker.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.Grid;
import com.calebtrevino.tallystacker.presenters.GridSettingPresenter;
import com.calebtrevino.tallystacker.presenters.GridSettingPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.GridSettingMapper;
import com.calebtrevino.tallystacker.views.GridSettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by fatal on 9/6/2016.
 */
public class GridSettingFragment extends GridHolderFragment implements GridSettingView, GridSettingMapper {

    GridSettingPresenter mGridSettingPresenter;

    public static GridHolderFragment newInstance() {
        return new GridSettingFragment();
    }

    @OnClick(R.id.editName)
    void editName() {
        animationAtStart();
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                mGridName.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        mGridName.requestFocus();
        mGridName.moveCursorToVisibleOffset();
        mGridName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mGridSettingPresenter.setGridName(mGridName.getText().toString());
                    animationAtEnd();
                    return true;
                }
                return false;
            }

        });
    }

    private void animationAtEnd() {
        mGridName.setEnabled(false);
        TranslateAnimation animate = new TranslateAnimation(mEditButton.getWidth(), 0, 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        mEditButton.startAnimation(animate);
        mEditButton.setVisibility(View.VISIBLE);
    }

    private void animationAtStart() {
        mGridName.setEnabled(true);
        TranslateAnimation animate = new TranslateAnimation(0, mEditButton.getWidth(), 0, 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        mEditButton.startAnimation(animate);
        mEditButton.setVisibility(View.GONE);
    }

    @BindView(R.id.gridName)
    TextInputEditText mGridName;

    @BindView(R.id.rowNo)
    TextView mRowNo;

    @BindView(R.id.columnNo)
    TextView mColumnNo;

    @BindView(R.id.lastUpdated)
    TextView mLastUpdated;

    @BindView(R.id.editName)
    ImageButton mEditButton;

    @BindView(R.id.updateSwitch)
    Switch mUpdateSwitch;

    @BindView(R.id.forceAddRecycle)
    RecyclerView mForceAddRecycler;

    @OnCheckedChanged(R.id.updateSwitch)
    void setUpdateSwitch() {
        mGridSettingPresenter.setKeepUpdates(mUpdateSwitch.isChecked());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGridSettingPresenter = new GridSettingPresenterImpl(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid_setting, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGridSettingPresenter.initializeViews();
        if (savedInstanceState != null) {
            mGridSettingPresenter.restoreState(savedInstanceState);
        }

        mGridSettingPresenter.initializeDatabase();
    }

    @Override
    public void added(Grid grid) {
        mGridSettingPresenter.changeGrid(grid);
    }

    @Override
    public void initializeToolbar() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.fragment_grid);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }

    @Override
    public void setGridName(String gridName) {
        mGridName.setText(gridName);
    }

    @Override
    public void setRowCount(String rowCount) {
        mRowNo.setText(rowCount);
    }

    @Override
    public void setColumnCount(String columnCount) {
        mColumnNo.setText(columnCount);
    }

    @Override
    public void setLastUpdatedDate(String lastUpdatedDate) {
        mLastUpdated.setText(lastUpdatedDate);
    }

    @Override
    public void setKeepUpdates(boolean keepUpdates) {
        mUpdateSwitch.setChecked(keepUpdates);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridSettingPresenter.saveState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGridSettingPresenter.releaseAllResources();
    }


    @Override
    public Parcelable getPositionState() {
        if (mForceAddRecycler != null) {
            return mForceAddRecycler.getLayoutManager().onSaveInstanceState();
        } else {
            return null;
        }
    }

    @Override
    public void setPositionState(Parcelable state) {
        if (mForceAddRecycler != null) {
            mForceAddRecycler.getLayoutManager().onRestoreInstanceState(state);
        }
    }


    @Override
    public void registerAdapter(final RecyclerView.Adapter<?> adapter) {
        if (mForceAddRecycler != null) {
            mForceAddRecycler.setAdapter(adapter);
        }
    }


    @Override
    public void initializeRecyclerLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mForceAddRecycler != null) {
            mForceAddRecycler.setLayoutManager(layoutManager);
        }
    }
}
