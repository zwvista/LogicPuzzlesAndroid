package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.field.DatabaseField;

public class LevelProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String gameID;
    @DatabaseField
    public String levelID;
    @DatabaseField
    public int moveIndex;
}
