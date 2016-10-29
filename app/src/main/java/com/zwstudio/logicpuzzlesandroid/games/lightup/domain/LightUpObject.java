package com.zwstudio.logicpuzzlesandroid.games.lightup.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class LightUpObject {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static LightUpObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new LightUpLightbulbObject();
            case "marker":
                return new LightUpMarkerObject();
            default:
                return new LightUpEmptyObject();
        }
    }
}
