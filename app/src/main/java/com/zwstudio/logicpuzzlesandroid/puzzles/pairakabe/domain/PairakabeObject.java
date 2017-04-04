package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class PairakabeObject {
    public abstract String objAsString();
    public static PairakabeObject objTypeFromString(String str) {
        switch (str) {
            case "wall":
                return new PairakabeWallObject();
            case "marker":
                return new PairakabeMarkerObject();
            default:
                return new PairakabeEmptyObject();
        }
    }
}
