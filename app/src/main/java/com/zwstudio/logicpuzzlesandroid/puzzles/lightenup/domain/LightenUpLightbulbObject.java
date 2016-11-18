package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightenUpLightbulbObject extends LightenUpObject {
    public LightenUpLightbulbState state = LightenUpLightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
