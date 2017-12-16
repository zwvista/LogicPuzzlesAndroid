package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class CarpentersWallCornerObject extends CarpentersWallObject {
    public HintState state = HintState.Normal;
    public int tiles;
    public boolean isHint() {return true;}
    public String objAsString() {
        return "corner";
    }
}
