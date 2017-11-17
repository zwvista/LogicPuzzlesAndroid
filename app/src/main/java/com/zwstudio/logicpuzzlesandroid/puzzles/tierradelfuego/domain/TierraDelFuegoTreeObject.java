package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class TierraDelFuegoTreeObject extends TierraDelFuegoObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
