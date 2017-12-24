package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;


public abstract class LighthousesObject {
    public abstract String objAsString();
    public static LighthousesObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new LighthousesMarkerObject();
            case "lighthouse":
                return new LighthousesLighthouseObject();
            default:
                return new LighthousesMarkerObject();
        }
    }
}
