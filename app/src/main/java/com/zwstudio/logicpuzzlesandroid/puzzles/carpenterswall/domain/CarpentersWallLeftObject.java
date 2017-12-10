package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class CarpentersWallLeftObject extends CarpentersWallObject {
    public HintState state = HintState.Normal;
    public boolean isHint() {return true;}
    public String objAsString() {
        return "left";
    }
}
