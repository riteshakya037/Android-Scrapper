package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePageAdapterMapper;
import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.views.adaptors.GridFragmentPagerAdapter;

/**
 * Created by fatal on 9/6/2016.
 */
public interface GridPagerMapper extends BasePageAdapterMapper, BasePositionStateMapper {

    void registerTabs(GridFragmentPagerAdapter mCatalogueAdapter);
}
