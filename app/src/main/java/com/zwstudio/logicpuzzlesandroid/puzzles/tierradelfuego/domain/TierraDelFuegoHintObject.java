package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TierraDelFuegoHintObject extends TierraDelFuegoObject {
    public HintState state = HintState.Normal;
    public char id;
    public String objAsString() {
        return "hint";
    }
}
