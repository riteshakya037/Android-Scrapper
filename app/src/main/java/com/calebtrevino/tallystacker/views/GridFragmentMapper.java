package com.calebtrevino.tallystacker.views;

import com.calebtrevino.tallystacker.presenters.bases.BaseAdapterMapper;
import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;

/**
 * Created by fatal on 9/6/2016.
 */
public interface GridFragmentMapper extends BaseAdapterMapper, BasePositionStateMapper {

    void registerTabs(GridFragmentPagerAdapter mCatalogueAdapter);
}
