package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TapAlikeWallObject extends TapAlikeObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
