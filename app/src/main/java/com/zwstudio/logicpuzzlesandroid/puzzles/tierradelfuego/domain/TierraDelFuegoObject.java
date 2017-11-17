package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class TierraDelFuegoObject {
    public abstract String objAsString();
    public static TierraDelFuegoObject objFromString(String str) {
        switch (str) {
            case "marker":
                return new TierraDelFuegoMarkerObject();
            case "tree":
                return new TierraDelFuegoTreeObject();
            default:
                return new TierraDelFuegoEmptyObject();
        }
    }
}
