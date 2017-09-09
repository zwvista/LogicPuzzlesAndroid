package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BWTapaWallObject extends BWTapaObject {
    public HintState state = HintState.Normal;
    public String objTypeAsString() {
        return "wall";
    }
}
