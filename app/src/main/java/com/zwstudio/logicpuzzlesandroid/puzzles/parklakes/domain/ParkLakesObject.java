package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class ParkLakesObject {
    public abstract String objAsString();
    public static ParkLakesObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new ParkLakesMarkerObject();
            case "tree":
                return new ParkLakesTreeObject();
            default:
                return new ParkLakesEmptyObject();
        }
    }
}
