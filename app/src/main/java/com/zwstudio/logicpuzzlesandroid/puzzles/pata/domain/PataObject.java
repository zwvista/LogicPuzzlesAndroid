package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain;

public abstract class PataObject {
    public abstract String objTypeAsString();
    public static PataObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new PataMarkerObject();
            case "wall":
                return new PataWallObject();
            default:
                return new PataEmptyObject();
        }
    }
}
