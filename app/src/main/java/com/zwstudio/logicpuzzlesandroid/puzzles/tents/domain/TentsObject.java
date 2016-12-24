package com.zwstudio.logicpuzzlesandroid.puzzles.tents.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class TentsObject {
    public abstract String objAsString();
    public static TentsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new TentsMarkerObject();
            case "tent":
                return new TentsTentObject();
            default:
                return new TentsEmptyObject();
        }
    }
}
