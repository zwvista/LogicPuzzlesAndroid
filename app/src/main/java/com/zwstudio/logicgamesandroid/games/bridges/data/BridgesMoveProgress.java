package com.zwstudio.logicgamesandroid.games.bridges.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zwstudio.logicgamesandroid.common.data.MoveProgress;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class BridgesMoveProgress extends MoveProgress {
    @DatabaseField
    public int rowFrom;
    @DatabaseField
    public int colFrom;
    @DatabaseField
    public int rowTo;
    @DatabaseField
    public int colTo;
}
