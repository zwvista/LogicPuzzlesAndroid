package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain;


public abstract class WallSentinelsObject {
    public String objAsString() {
        return "empty";
    }
    public static WallSentinelsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new WallSentinelsMarkerObject();
            case "wall":
                return new WallSentinelsWallObject();
            default:
                return new WallSentinelsEmptyObject();
        }
    }
}
