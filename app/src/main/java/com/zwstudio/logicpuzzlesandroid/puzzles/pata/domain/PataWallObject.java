package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class PataWallObject extends PataObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
