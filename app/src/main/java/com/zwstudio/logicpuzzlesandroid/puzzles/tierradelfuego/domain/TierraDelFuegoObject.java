package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

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
