package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class WallsObject {
    public abstract String objAsString();
    public static WallsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new WallsMarkerObject();
            case "tree":
                return new WallsTreeObject();
            default:
                return new WallsEmptyObject();
        }
    }
}
