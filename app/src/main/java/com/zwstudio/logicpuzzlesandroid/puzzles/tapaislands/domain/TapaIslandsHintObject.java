package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TapaIslandsHintObject extends TapaIslandsObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
