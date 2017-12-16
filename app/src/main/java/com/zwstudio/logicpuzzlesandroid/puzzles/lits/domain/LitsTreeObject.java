package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class LitsTreeObject extends LitsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
