package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BootyIslandTreasureObject extends BootyIslandObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "treasure";
    }
}
