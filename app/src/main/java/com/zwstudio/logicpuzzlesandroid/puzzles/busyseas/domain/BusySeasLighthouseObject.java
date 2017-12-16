package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class BusySeasLighthouseObject extends BusySeasObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "lighthouse";
    }
}
