package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * Created by fatal on 9/6/2016.
 */
public interface GridCalendarMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper{
    void setMonthYear(String monthyear);
}