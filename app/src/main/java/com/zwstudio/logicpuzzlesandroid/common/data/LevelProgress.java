package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LevelProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
}
