package com.calebtrevino.tallystacker.views;

import android.support.v4.app.FragmentManager;

import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseEmptyRelativeLayoutView;
import com.calebtrevino.tallystacker.views.bases.BasePageViewView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

/**
 * Created by fatal on 9/6/2016.
 */
public interface GridFragmentView extends BaseContextView, BaseToolbarView, BaseEmptyRelativeLayoutView, BasePageViewView {

    FragmentManager getFragmentManager();
}
