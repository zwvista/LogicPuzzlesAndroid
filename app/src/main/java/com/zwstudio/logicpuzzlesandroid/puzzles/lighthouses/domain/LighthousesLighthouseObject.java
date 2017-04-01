package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LighthousesLighthouseObject extends LighthousesObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "lighthouse";
    }
}
