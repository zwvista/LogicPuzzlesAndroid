package com.zwstudio.logicgamesandroid.lightup.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zwstudio.logicgamesandroid.logicgames.data.GameProgress;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class LightUpGameProgress extends GameProgress {
    @DatabaseField
    public int markerOption;
    @DatabaseField
    public boolean normalLightbulbsOnly;
}
