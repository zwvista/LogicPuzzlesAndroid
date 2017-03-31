package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain;


/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class BusySeasObject {
    public abstract String objAsString();
    public static BusySeasObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new BusySeasMarkerObject();
            case "tower":
                return new BusySeasTowerObject();
            default:
                return new BusySeasMarkerObject();
        }
    }
}
