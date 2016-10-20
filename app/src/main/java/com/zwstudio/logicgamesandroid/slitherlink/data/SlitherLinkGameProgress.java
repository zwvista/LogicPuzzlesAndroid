package com.zwstudio.logicgamesandroid.slitherlink.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zwvista on 2016/09/29.
 */

@DatabaseTable
public class SlitherLinkGameProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String levelID = "Level 1";
    @DatabaseField
    public int markerOption;
    @DatabaseField
    public boolean normalLightbulbsOnly;
}
