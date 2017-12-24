package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;


public abstract class SentinelsObject {
    public abstract String objAsString();
    public static SentinelsObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new SentinelsMarkerObject();
            case "tower":
                return new SentinelsTowerObject();
            default:
                return new SentinelsMarkerObject();
        }
    }
}
