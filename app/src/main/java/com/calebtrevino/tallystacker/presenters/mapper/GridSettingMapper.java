package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.models.enums.GridMode;
import com.calebtrevino.tallystacker.presenters.bases.BasePositionStateMapper;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface GridSettingMapper extends BaseRecycleAdapterMapper, BasePositionStateMapper {
    void setGridName(String gridName);

    void setRowCount(String rowCount);

    void setColumnCount(String columnCount);

    void setGridModeValues(GridMode gridMode, int gridTallyCount);

    void setLastUpdatedDate(String lastUpdatedDate);

    void setKeepUpdates(boolean keepUpdates);
}
