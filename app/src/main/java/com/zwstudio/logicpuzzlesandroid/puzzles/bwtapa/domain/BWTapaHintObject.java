package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class BWTapaHintObject extends BWTapaObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
