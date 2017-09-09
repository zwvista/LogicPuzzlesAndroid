package com.zwstudio.logicpuzzlesandroid.puzzles.pata.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class PataHintObject extends PataObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
