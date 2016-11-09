package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NurikabeLightbulbObject extends NurikabeObject {
    public NurikabeLightbulbState state = NurikabeLightbulbState.Normal;
    public String objTypeAsString() {
        return "lightbulb";
    }
}
