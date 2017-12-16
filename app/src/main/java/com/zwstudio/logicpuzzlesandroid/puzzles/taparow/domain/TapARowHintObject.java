package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapARowHintObject extends TapARowObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
