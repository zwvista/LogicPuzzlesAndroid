package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class TapaIslandsObject {
    public abstract String objTypeAsString();
    public static TapaIslandsObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new TapaIslandsMarkerObject();
            case "wall":
                return new TapaIslandsWallObject();
            default:
                return new TapaIslandsEmptyObject();
        }
    }
}
