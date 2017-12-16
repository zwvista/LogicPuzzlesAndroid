package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class LightenUpWallObject extends LightenUpObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
