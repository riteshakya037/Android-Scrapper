package com.calebtrevino.tallystacker.presenters.mapper;

import android.widget.ArrayAdapter;

import com.calebtrevino.tallystacker.presenters.bases.BasePageAdapterMapper;
import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;

/**
 * @author Ritesh Shakya
 */
public interface GridPagerMapper extends BasePageAdapterMapper, BasePositionStateMapper {

    void registerSpinner(ArrayAdapter adapter);

    void initializeSpinnerListener();

    void registerTabs(GridFragmentPagerAdapter mCatalogueAdapter);

    void setSpinnerLast();
}
