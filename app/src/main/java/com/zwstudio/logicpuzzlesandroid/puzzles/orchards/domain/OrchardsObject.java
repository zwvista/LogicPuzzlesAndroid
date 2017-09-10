package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

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
