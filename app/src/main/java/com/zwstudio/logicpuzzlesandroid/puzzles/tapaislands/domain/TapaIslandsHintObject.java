package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapaIslandsHintObject extends TapaIslandsObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
