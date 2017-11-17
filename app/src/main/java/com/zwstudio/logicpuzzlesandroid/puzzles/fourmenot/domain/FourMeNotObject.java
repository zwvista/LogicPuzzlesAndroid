package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class FourMeNotObject {
    public abstract String objAsString();
    public static FourMeNotObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new FourMeNotMarkerObject();
            case "tree":
                return new FourMeNotTreeObject();
            default:
                return new FourMeNotEmptyObject();
        }
    }
}
