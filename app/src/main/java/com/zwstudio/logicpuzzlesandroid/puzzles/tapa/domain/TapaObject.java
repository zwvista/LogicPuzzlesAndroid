package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain;

public abstract class TapaObject {
    public abstract String objTypeAsString();
    public static TapaObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new TapaMarkerObject();
            case "wall":
                return new TapaWallObject();
            default:
                return new TapaEmptyObject();
        }
    }
}
