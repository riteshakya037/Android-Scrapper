package com.calebtrevino.tallystacker.views.custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.models.listeners.FinishedListener;
import com.calebtrevino.tallystacker.presenters.DialogPresenter;
import com.calebtrevino.tallystacker.presenters.DialogPresenterImpl;
import com.calebtrevino.tallystacker.presenters.mapper.AddGridMapper;
import com.calebtrevino.tallystacker.utils.Utils;
import com.calebtrevino.tallystacker.views.DialogView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by fatal on 9/9/2016.
 */
public class CreateNewGridDialog extends Dialog implements DialogView, AddGridMapper {

    private Activity mActivity;
    private DialogPresenter dialogPresenter;

    @BindView(R.id.gridName)
    TextView gridName;
    private FinishedListener listener;

    @OnClick(R.id.fab)
    public void createGrid() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Creating Grid");
        dialog.show();
        listener.onFinished(dialogPresenter.getGrid(), dialog);
    }

    @OnClick(R.id.addLeague)
    public void createLeague() {
        dialogPresenter.createLeague();
    }

    @BindView(R.id.rowNo)
    TextView rowNo;

    @BindView(R.id.columnNo)
    TextView columnNo;

    @BindView(R.id.leagueRecycler)
    RecyclerView mLeagueRecycler;

    @OnClick(R.id.backButton)
    public void dispose() {
        dismiss();
    }

    public CreateNewGridDialog(Activity activity) {
        super(activity);
        mActivity = activity;
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
        return rowNo.getText().toString();
    }

    @Override
    public String getName() {
        return gridName.getText().toString();
    }

}
