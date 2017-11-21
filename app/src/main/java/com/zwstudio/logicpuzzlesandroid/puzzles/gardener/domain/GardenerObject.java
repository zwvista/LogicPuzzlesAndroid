package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class GardenerObject {
    public abstract String objAsString();
    public static GardenerObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new GardenerMarkerObject();
            case "tree":
                return new GardenerTreeObject();
            default:
                return new GardenerEmptyObject();
        }
    }
}
