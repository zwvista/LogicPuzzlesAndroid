package com.zwstudio.logicgamesandroid.logicgames.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class LogicGamesGameProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String gameName = "LightUp";
    @DatabaseField
    public boolean playMusic = true;
    @DatabaseField
    public boolean playSound = true;
}
