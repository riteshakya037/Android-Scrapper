package com.calebtrevino.tallystacker.presenters.mapper;

import com.calebtrevino.tallystacker.models.Game;
import com.calebtrevino.tallystacker.presenters.bases.BaseRecycleAdapterMapper;

import java.util.List;

/**
 * @author Ritesh Shakya
 */
public interface AddGridMapper extends BaseRecycleAdapterMapper {
    String getRowNo();

    String getColumnNo();

    String getName();

    void setName(String gridName);

    List<Game> getGames();
}
