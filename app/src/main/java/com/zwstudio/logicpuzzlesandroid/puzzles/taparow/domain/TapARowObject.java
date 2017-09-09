package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class TapARowObject {
    public abstract String objTypeAsString();
    public static TapARowObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new TapARowMarkerObject();
            case "wall":
                return new TapARowWallObject();
            default:
                return new TapARowEmptyObject();
        }
    }
}
