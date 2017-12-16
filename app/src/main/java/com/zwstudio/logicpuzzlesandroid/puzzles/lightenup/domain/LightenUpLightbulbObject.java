package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class LightenUpLightbulbObject extends LightenUpObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
