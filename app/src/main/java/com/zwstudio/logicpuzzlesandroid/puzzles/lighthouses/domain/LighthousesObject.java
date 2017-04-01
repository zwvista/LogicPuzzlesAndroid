package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;


/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

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
