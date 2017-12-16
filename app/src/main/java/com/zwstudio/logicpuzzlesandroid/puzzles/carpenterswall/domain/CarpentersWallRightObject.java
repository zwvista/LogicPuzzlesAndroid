package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class CarpentersWallRightObject extends CarpentersWallObject {
    public HintState state = HintState.Normal;
    public boolean isHint() {return true;}
    public String objAsString() {
        return "right";
    }
}
