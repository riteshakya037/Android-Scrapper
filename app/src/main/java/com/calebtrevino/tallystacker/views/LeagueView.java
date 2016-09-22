package com.calebtrevino.tallystacker.views;

import android.support.v4.app.FragmentManager;

import com.calebtrevino.tallystacker.views.bases.BaseContextView;
import com.calebtrevino.tallystacker.views.bases.BaseEmptyRelativeLayoutView;
import com.calebtrevino.tallystacker.views.bases.BasePageViewView;
import com.calebtrevino.tallystacker.views.bases.BaseToolbarView;

/**
 * @author Ritesh Shakya
 */
public interface LeagueView extends BaseContextView, BaseToolbarView, BaseEmptyRelativeLayoutView, BasePageViewView,BaseMainUIView {
    FragmentManager getFragmentManager();
}
