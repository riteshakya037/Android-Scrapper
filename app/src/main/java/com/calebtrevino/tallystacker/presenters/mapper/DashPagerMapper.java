package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * Created by Ritesh on 0012, May 12, 2017.
 */

public interface DashPagerMapper extends BasePositionStateMapper, BaseRecycleAdapterMapper {
    int getPosition();
}
