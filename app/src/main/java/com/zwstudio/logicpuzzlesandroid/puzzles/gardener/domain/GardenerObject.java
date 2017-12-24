package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain;

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
