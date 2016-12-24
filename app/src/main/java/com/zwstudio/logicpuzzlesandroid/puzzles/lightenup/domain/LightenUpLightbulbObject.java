package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightenUpLightbulbObject extends LightenUpObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
