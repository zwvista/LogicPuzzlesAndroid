package com.zwstudio.logicpuzzlesandroid.games.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightUpLightbulbObject extends LightUpObject {
    public LightUpLightbulbState state = LightUpLightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
