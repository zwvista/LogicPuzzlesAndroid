package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapAlikeWallObject extends TapAlikeObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
