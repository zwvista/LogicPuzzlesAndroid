package com.zwstudio.logicpuzzlesandroid.puzzles.parks.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class ParksObject {
    public abstract String objAsString();
    public static ParksObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new ParksMarkerObject();
            case "tree":
                return new ParksTreeObject();
            default:
                return new ParksEmptyObject();
        }
    }
}
