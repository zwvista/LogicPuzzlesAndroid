package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class BootyIslandTreasureObject extends BootyIslandObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "treasure";
    }
}
