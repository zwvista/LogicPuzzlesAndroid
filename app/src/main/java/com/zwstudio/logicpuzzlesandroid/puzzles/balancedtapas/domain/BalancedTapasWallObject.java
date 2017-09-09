package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BalancedTapasWallObject extends BalancedTapasObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
