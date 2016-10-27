package com.zwstudio.logicgamesandroid.clouds.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zwstudio.logicgamesandroid.logicgames.data.MoveProgress;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class CloudsMoveProgress extends MoveProgress {
    @DatabaseField
    public int row;
    @DatabaseField
    public int col;
    @DatabaseField
    public int obj;
}
