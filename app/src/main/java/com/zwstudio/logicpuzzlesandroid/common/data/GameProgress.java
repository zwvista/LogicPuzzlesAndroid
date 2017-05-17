package com.zwstudio.logicpuzzlesandroid.common.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by zwvista on 2016/09/29.
 */

public class GameProgress implements java.io.Serializable {
    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField
    public String gameID;
    @DatabaseField
    public String levelID = "1";
    @DatabaseField
    public String option1;
    @DatabaseField
    public String option2;
    @DatabaseField
    public String option3;
}
