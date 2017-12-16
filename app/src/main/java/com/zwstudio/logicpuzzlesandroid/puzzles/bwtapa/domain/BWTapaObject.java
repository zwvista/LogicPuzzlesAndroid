package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain;

public abstract class BWTapaObject {
    public abstract String objTypeAsString();
    public static BWTapaObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new BWTapaMarkerObject();
            case "wall":
                return new BWTapaWallObject();
            default:
                return new BWTapaEmptyObject();
        }
    }
}
