package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain;

public abstract class BalancedTapasObject {
    public abstract String objTypeAsString();
    public static BalancedTapasObject objTypeFromString(String str) {
        switch (str) {
            case "marker":
                return new BalancedTapasMarkerObject();
            case "wall":
                return new BalancedTapasWallObject();
            default:
                return new BalancedTapasEmptyObject();
        }
    }
}
