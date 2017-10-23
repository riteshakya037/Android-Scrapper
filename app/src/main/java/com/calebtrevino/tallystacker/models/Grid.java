package com.calebtrevino.tallystacker.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.calebtrevino.tallystacker.models.base.BaseModel;
import com.calebtrevino.tallystacker.models.enums.GridMode;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ritesh Shakya
 */
public class Grid extends BaseModel implements Parcelable {
    public static final Creator<Grid> CREATOR = new Creator<Grid>() {
        @Override public Grid createFromParcel(Parcel in) {
            return new Grid(in);
        }

        @Override public Grid[] newArray(int size) {
            return new Grid[size];
        }
    };
    private long _id;
    private String gridName;
    private int rowNo;
    private int columnNo;
    private List<Game> gameList;
    private boolean keepUpdates;
    private List<GridLeagues> gridLeagues;
    private long updatedOn;
    private GridMode gridMode;
    private int gridTotalCount;

    public Grid() {
    }

    protected Grid(Parcel in) {
        _id = in.readLong();
        gridName = in.readString();
        rowNo = in.readInt();
        columnNo = in.readInt();
        gameList = in.createTypedArrayList(Game.CREATOR);
        keepUpdates = in.readByte() != 0;
        gridLeagues = in.createTypedArrayList(GridLeagues.CREATOR);
        updatedOn = in.readLong();
        gridMode = in.readParcelable(GridMode.class.getClassLoader());
        gridTotalCount = in.readInt();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(gridName);
        dest.writeInt(rowNo);
        dest.writeInt(columnNo);
        dest.writeTypedList(gameList);
        dest.writeByte((byte) (keepUpdates ? 1 : 0));
        dest.writeTypedList(gridLeagues);
        dest.writeLong(updatedOn);
        dest.writeParcelable(gridMode, flags);
        dest.writeInt(gridTotalCount);
    }

    @Override public int describeContents() {
        return 0;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    @Override public void createID() {
        this._id = hashCode();
    }

    @Override public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("grid_name", getGridName());
            jsonObject.put("row_no", getRowNo());
            jsonObject.put("column_no", getColumnNo());
            jsonObject.put("game_list", Game.getIDArrayToJSSON(getGameList()));
            jsonObject.put("keep_updates", isKeepUpdates());
            jsonObject.put("grid_leagues", GridLeagues.createJsonArray(getGridLeagues()));
            jsonObject.put("updated_on", getUpdatedOn());
            jsonObject.put("grid_mode", getGridMode().getValue());
            jsonObject.put("grid_total_count", getGridTotalCount());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public boolean isKeepUpdates() {
        return keepUpdates;
    }

    public void setKeepUpdates(boolean keepUpdates) {
        this.keepUpdates = keepUpdates;
    }

    public List<GridLeagues> getGridLeagues() {
        return gridLeagues;
    }

    public void setGridLeagues(List<GridLeagues> gridLeagues) {
        this.gridLeagues = gridLeagues;
    }

    public long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(long updatedOn) {
        this.updatedOn = updatedOn;
    }

    public GridMode getGridMode() {
        return gridMode;
    }

    public void setGridMode(GridMode gridMode) {
        this.gridMode = gridMode;
    }

    public int getGridTotalCount() {
        return gridTotalCount;
    }

    public void setGridTotalCount(int gridTotalCount) {
        this.gridTotalCount = gridTotalCount == 0 ? 3 : gridTotalCount;
    }
}
