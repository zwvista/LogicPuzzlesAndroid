package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class ParksTreeObject extends ParksObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
