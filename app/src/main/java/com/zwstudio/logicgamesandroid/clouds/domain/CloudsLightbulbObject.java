package com.zwstudio.logicgamesandroid.clouds.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CloudsLightbulbObject extends CloudsObject {
    public CloudsLightbulbState state = CloudsLightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
