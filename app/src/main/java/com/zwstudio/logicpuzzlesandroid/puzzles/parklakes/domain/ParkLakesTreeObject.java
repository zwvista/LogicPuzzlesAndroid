package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class ParkLakesTreeObject extends ParkLakesObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
