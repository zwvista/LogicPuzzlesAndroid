package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapaHintObject extends TapaObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
