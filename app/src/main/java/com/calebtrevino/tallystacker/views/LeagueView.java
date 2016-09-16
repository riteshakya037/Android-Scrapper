package com.calebtrevino.tallystacker.views;

import android.support.v4.app.FragmentManager;

import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseEmptyRelativeLayoutView;
import com.calebtrevino.tallystacker.views.bases.BasePageViewView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

/**
 * Created by fatal on 9/16/2016.
 */
public interface LeagueView extends BaseContextView, BaseToolbarView, BaseEmptyRelativeLayoutView, BasePageViewView {
    FragmentManager getFragmentManager();
}
