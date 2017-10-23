package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface GridCalendarMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper {
    void setMonthYear(String monthYear);
}
