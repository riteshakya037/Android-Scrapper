package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * @author Ritesh Shakya
 */
public interface AddGridMapper extends BaseRecycleAdapterMapper {
    String getRowNo();

    String getColumnNo();

    String getName();
}
