package com.zwstudio.logicgamesandroid.games.slitherlink.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zwstudio.logicgamesandroid.common.data.MoveProgress;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class SlitherLinkMoveProgress extends MoveProgress {
    @DatabaseField
    public int row;
    @DatabaseField
    public int col;
    @DatabaseField
    public int objOrientation;
    @DatabaseField
    public int obj;
}
