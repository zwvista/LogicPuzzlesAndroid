package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class FourMeNotTreeObject extends FourMeNotObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
