package com.zwstudio.logicpuzzlesandroid.puzzles.minilits.domain;

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
