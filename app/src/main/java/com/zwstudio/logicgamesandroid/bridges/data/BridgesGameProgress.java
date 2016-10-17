package com.zwstudio.logicgamesandroid.bridges.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class BridgesGameProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID = "Level 1";
}
