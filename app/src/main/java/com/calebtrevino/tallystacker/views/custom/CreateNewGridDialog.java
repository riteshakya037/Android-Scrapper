package com.calebtrevino.tallystacker.views.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.calebtrevino.tallystacker.R;
import com.calebtrevino.tallystacker.utils.Utils;


/**
 * Created by fatal on 9/9/2016.
 */
public class CreateNewGridDialog extends Dialog {
    public CreateNewGridDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_grid_dialog);
        Utils utils = new Utils(getContext());
        getWindow().setLayout(utils.getScreenWidth(), WindowManager.LayoutParams.MATCH_PARENT);
    }
}
