package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class TierraDelFuegoHintObject extends TierraDelFuegoObject {
    public HintState state = HintState.Normal;
    public char id;
    public String objAsString() {
        return "hint";
    }
}
