package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class TapAlikeObject {
    public abstract String objTypeAsString();
    public static TapAlikeObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new TapAlikeMarkerObject();
            case "wall":
                return new TapAlikeWallObject();
            default:
                return new TapAlikeEmptyObject();
        }
    }
}
