package com.zwstudio.logicgamesandroid.bridges.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class BridgesMoveProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
    @DatabaseField
    public int rowFrom;
    @DatabaseField
    public int colFrom;
    @DatabaseField
    public int rowTo;
    @DatabaseField
    public int colTo;
}
