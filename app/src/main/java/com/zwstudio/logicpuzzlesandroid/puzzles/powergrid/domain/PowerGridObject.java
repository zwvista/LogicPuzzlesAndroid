package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;


/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class PowerGridObject {
    public abstract String objAsString();
    public static PowerGridObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new PowerGridMarkerObject();
            case "tower":
                return new PowerGridPostObject();
            default:
                return new PowerGridMarkerObject();
        }
    }
}
