package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain;

public abstract class PairakabeObject {
    public abstract String objAsString();
    public static PairakabeObject objTypeFromString(String str) {
        switch (str) {
            case "wall":
                return new PairakabeWallObject();
            case "marker":
                return new PairakabeMarkerObject();
            default:
                return new PairakabeEmptyObject();
        }
    }
}
