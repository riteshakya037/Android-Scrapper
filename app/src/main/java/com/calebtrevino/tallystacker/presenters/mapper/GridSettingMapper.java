package com.calebtrevino.tallystacker.presenters.mapper;

/**
 * Created by fatal on 9/16/2016.
 */
public interface GridSettingMapper {
    void setGridName(String gridName);

    void setRowCount(String rowCount);

    void setColumnCount(String columnCount);

    void setLastUpdatedDate(String lastUpdatedDate);

    void setKeepUpdates(boolean keepUpdates);

    void setForceAdd(boolean forceAdd);
}
