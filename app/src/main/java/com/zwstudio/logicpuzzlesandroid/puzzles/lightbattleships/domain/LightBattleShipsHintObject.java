package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class LightBattleShipsHintObject extends LightBattleShipsObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
