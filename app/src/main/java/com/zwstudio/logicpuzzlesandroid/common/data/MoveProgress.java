package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MoveProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String gameID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
    @DatabaseField
    public int row;
    @DatabaseField
    public int col;
    @DatabaseField
    public int row2;
    @DatabaseField
    public int col2;
    @DatabaseField
    public int intValue1;
    @DatabaseField
    public int intValue2;
    @DatabaseField
    public int intValue3;
    @DatabaseField
    public String strValue1;
    @DatabaseField
    public String strValue2;
    @DatabaseField
    public String strValue3;
}
