package com.zwstudio.logicgamesandroid.hitori.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zwstudio.logicgamesandroid.logicgames.data.GameProgress;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class HitoriGameProgress extends GameProgress {
    @DatabaseField
    public int markerOption;
}
