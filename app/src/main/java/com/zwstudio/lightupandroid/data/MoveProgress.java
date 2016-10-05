package com.zwstudio.lightupandroid.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class MoveProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public Integer moveIndex;
    @DatabaseField
    public Integer row;
    @DatabaseField
    public Integer col;
    @DatabaseField
    public String objTypeAsString;
}
