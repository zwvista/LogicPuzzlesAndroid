package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class NurikabeHintObject extends NurikabeObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
