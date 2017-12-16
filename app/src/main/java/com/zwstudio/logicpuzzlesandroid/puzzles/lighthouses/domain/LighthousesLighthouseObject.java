package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class LighthousesLighthouseObject extends LighthousesObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "lighthouse";
    }
}
