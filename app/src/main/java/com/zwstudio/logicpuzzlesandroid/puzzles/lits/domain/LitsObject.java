package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class LitsObject {
    public abstract String objAsString();
    public static LitsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new LitsMarkerObject();
            case "tree":
                return new LitsTreeObject();
            default:
                return new LitsEmptyObject();
        }
    }
}
