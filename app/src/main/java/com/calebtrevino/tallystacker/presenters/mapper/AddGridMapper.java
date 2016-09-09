package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

/**
 * Created by fatal on 9/9/2016.
 */
public interface AddGridMapper extends BaseRecycleAdapterMapper {
    String getRowNo();

    String getColumnNo();

    String getName();
}
