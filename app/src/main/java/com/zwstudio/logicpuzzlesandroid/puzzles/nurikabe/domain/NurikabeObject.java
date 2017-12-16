package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

public abstract class NurikabeObject {
    public abstract String objAsString();
    public static NurikabeObject objTypeFromString(String str) {
        switch (str) {
            case "wall":
                return new NurikabeWallObject();
            case "marker":
                return new NurikabeMarkerObject();
            default:
                return new NurikabeEmptyObject();
        }
    }
}
