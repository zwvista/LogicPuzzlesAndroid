package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class PataHintObject extends PataObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
