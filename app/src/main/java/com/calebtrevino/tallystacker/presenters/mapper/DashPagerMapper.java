package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */

public interface DashPagerMapper extends BasePositionStateMapper, BaseRecycleAdapterMapper {
    int getPosition();
}
