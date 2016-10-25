package com.zwstudio.logicgamesandroid.clouds.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class CloudsObject {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static CloudsObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new CloudsLightbulbObject();
            case "marker":
                return new CloudsMarkerObject();
            default:
                return new CloudsEmptyObject();
        }
    }
}
