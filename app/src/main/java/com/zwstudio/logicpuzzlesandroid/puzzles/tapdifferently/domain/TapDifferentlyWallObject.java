package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapDifferentlyWallObject extends TapDifferentlyObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
