package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain;


public abstract class BusySeasObject {
    public abstract String objAsString();
    public static BusySeasObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new BusySeasMarkerObject();
            case "lighthouse":
                return new BusySeasLighthouseObject();
            default:
                return new BusySeasMarkerObject();
        }
    }
}
