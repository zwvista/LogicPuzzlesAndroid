package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class MathraxObject {
    public abstract String objAsString();
    public static MathraxObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new MathraxMarkerObject();
            case "tree":
                return new MathraxTreeObject();
            default:
                return new MathraxEmptyObject();
        }
    }
}
