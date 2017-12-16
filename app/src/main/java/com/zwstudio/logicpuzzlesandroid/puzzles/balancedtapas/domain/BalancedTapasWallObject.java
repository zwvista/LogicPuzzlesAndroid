package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class BalancedTapasWallObject extends BalancedTapasObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
