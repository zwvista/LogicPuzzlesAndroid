package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PowerGridHintObject extends PowerGridObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
