package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class CarpentersWallObject {
    public abstract String objAsString();
    public boolean isHint() {return false;}
    public static CarpentersWallObject objTypeFromString(String str) {
        switch (str) {
            case "wall":
                return new CarpentersWallWallObject();
            case "marker":
                return new CarpentersWallMarkerObject();
            default:
                return new CarpentersWallEmptyObject();
        }
    }
}
