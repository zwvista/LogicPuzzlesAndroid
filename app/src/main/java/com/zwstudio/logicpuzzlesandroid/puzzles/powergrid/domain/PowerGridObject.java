package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;


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
