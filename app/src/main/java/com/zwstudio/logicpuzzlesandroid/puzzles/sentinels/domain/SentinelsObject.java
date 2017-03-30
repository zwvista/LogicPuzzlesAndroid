package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;


/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

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
