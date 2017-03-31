package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;


/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

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
