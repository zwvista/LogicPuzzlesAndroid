package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

public abstract class LightenUpObject {
    public int lightness = 0;
    public abstract String objTypeAsString();
    public static LightenUpObject objTypeFromString(String str) {
        switch (str) {
            case "lightbulb":
                return new LightenUpLightbulbObject();
            case "marker":
                return new LightenUpMarkerObject();
            default:
                return new LightenUpEmptyObject();
        }
    }
}
