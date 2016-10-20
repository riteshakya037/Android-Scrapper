package com.calebtrevino.tallystacker.views.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.controllers.factories.DefaultFactory;
import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.models.listeners.FinishedListener;
import com.calebtrevino.tallystacker.presenters.DialogPresenter;
import com.calebtrevino.tallystacker.presenters.DialogPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.AddGridMapper;
import com.calebtrevino.tallystacker.utils.Utils;
import com.calebtrevino.tallystacker.views.DialogView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Ritesh Shakya
 */
public class CreateNewGridDialog extends Dialog implements DialogView, AddGridMapper {

    private final Activity mActivity;
    private List<Game> gameList;
    private DialogPresenter dialogPresenter;

    @BindView(R.id.gridName)
    TextInputEditText gridName;
    private FinishedListener listener;

    @OnClick(R.id.fab)
    void createGrid() {
        listener.onFinished(dialogPresenter.getGrid());
    }

    @OnClick(R.id.addLeague)
    void createLeague() {
        dialogPresenter.createLeague();
    }

    @BindView(R.id.rowNo)
    TextInputEditText rowNo;

    @BindView(R.id.columnNo)
    TextInputEditText columnNo;

    @BindView(R.id.leagueRecycler)
    RecyclerView mLeagueRecycler;

    @OnClick(R.id.backButton)
    void dispose() {
        dismiss();
    }

    public CreateNewGridDialog(Activity activity, List<Game> gameList) {
        super(activity);
        mActivity = activity;
        this.gameList = gameList;
    }

    public void setFinishedListener(FinishedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_grid_dialog);
        Utils utils = new Utils(getContext());
        getWindow().setLayout(utils.getScreenWidth(), WindowManager.LayoutParams.MATCH_PARENT);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        dialogPresenter = new DialogPresenterImpl(this, this);
        ButterKnife.bind(this);

        dialogPresenter.initializeViews();
        dialogPresenter.initializeDatabase();
        dialogPresenter.initializeDataFromPreferenceSource();

        setDefaultValues();
    }

    private void setDefaultValues() {
        rowNo.setText(String.valueOf(DefaultFactory.Grid.ROW_NO));
        columnNo.setText(String.valueOf(DefaultFactory.Grid.COLUMN_NO));
    }


    @Override
    public void hide() {
        super.hide();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogPresenter.releaseAllResources();
    }

    @Override
    public void registerAdapter(RecyclerView.Adapter<?> adapter) {
        if (mLeagueRecycler != null) {
            mLeagueRecycler.setAdapter(adapter);
        }
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void initializeRecyclerLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mLeagueRecycler != null) {
            mLeagueRecycler.setLayoutManager(layoutManager);
        }
    }

    @Override
    public String getRowNo() {
        return rowNo.getText().toString();
    }

    @Override
    public String getColumnNo() {
        return columnNo.getText().toString();
    }

    @Override
    public String getName() {
        return gridName.getText().toString();
    }

    @Override
    public void setName(String name) {
        gridName.setText(name);
    }

    @Override
    public List<Game> getGames() {
        return gameList;
    }

}
