package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * Created by fatal on 9/16/2016.
 */
public interface GridSettingMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper {
    void setGridName(String gridName);

    void setRowCount(String rowCount);

    void setColumnCount(String columnCount);

    void setLastUpdatedDate(String lastUpdatedDate);

    void setKeepUpdates(boolean keepUpdates);

}
