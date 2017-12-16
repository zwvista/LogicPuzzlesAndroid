package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class OrchardsTreeObject extends OrchardsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
