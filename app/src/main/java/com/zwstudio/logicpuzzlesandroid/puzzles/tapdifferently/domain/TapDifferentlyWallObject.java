package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapDifferentlyWallObject extends TapDifferentlyObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
