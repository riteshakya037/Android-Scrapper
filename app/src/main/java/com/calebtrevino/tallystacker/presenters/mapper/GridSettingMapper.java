package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface GridSettingMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper {
    void setGridName(String gridName);

    void setRowCount(String rowCount);

    void setColumnCount(String columnCount);

    void setGridMode(String gridMode);

    void setLastUpdatedDate(String lastUpdatedDate);

    void setKeepUpdates(boolean keepUpdates);

}
