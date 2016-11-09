package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public abstract class NurikabeObject {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static NurikabeObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new NurikabeLightbulbObject();
            case "marker":
                return new NurikabeMarkerObject();
            default:
                return new NurikabeEmptyObject();
        }
    }
}
