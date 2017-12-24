package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

public abstract class WallsObject {
    public abstract String objAsString();
    public static WallsObject objFromString(String str) {
        switch (str) {
            case "horz":
                return new WallsHorzObject();
            case "vert":
                return new WallsVertObject();
            default:
                return new WallsEmptyObject();
        }
    }
}
