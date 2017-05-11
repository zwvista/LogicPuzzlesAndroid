package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class MiniLitsObject {
    public abstract String objAsString();
    public static MiniLitsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new MiniLitsMarkerObject();
            case "tree":
                return new MiniLitsTreeObject();
            default:
                return new MiniLitsEmptyObject();
        }
    }
}
