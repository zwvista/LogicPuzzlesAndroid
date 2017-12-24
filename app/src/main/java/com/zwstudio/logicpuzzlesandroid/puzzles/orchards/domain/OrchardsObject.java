package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain;

public abstract class OrchardsObject {
    public abstract String objAsString();
    public static OrchardsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new OrchardsMarkerObject();
            case "tree":
                return new OrchardsTreeObject();
            default:
                return new OrchardsEmptyObject();
        }
    }
}
