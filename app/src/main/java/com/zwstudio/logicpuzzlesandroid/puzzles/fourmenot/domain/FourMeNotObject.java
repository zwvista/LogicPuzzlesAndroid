package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain;

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
