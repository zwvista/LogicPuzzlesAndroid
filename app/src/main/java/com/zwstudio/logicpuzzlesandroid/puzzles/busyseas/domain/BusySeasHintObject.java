package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class BusySeasHintObject extends BusySeasObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
