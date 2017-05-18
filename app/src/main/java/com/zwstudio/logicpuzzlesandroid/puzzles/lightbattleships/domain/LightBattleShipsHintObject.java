package com.zwstudio.logicpuzzlesandroid.puzzles.lightbattleships.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class LightBattleShipsHintObject extends LightBattleShipsObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
