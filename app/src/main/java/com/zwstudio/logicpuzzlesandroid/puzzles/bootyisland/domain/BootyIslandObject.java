package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;


public abstract class BootyIslandObject {
    public abstract String objAsString();
    public static BootyIslandObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new BootyIslandMarkerObject();
            case "treasure":
                return new BootyIslandTreasureObject();
            default:
                return new BootyIslandMarkerObject();
        }
    }
}
