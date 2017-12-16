package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

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
