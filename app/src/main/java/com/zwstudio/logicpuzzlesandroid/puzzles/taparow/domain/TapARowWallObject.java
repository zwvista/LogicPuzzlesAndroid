package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapARowWallObject extends TapARowObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
