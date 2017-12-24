package com.zwstudio.logicpuzzlesandroid.puzzles.lits.domain;

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
