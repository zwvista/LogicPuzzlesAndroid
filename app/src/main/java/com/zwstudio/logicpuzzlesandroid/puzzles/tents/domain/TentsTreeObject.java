package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class TentsTreeObject extends TentsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
