package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class HolidayIslandObject {
    public abstract String objAsString();
    public static HolidayIslandObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new HolidayIslandMarkerObject();
            case "tree":
                return new HolidayIslandTreeObject();
            default:
                return new HolidayIslandEmptyObject();
        }
    }
}
