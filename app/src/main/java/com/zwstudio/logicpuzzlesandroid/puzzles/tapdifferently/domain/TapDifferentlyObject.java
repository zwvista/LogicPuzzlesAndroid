package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain;

public abstract class TapDifferentlyObject {
    public abstract String objTypeAsString();
    public static TapDifferentlyObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new TapDifferentlyMarkerObject();
            case "wall":
                return new TapDifferentlyWallObject();
            default:
                return new TapDifferentlyEmptyObject();
        }
    }
}
