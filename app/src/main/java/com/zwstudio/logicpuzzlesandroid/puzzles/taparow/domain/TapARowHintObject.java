package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapARowHintObject extends TapARowObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "hint";
    }
}
