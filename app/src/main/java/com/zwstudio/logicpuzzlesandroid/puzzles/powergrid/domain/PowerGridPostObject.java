package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class PowerGridPostObject extends PowerGridObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tower";
    }
}
