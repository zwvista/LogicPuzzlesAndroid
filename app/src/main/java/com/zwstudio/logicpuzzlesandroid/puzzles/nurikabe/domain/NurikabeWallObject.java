package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NurikabeWallObject extends NurikabeObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
