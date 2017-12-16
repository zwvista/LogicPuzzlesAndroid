package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class TierraDelFuegoTreeObject extends TierraDelFuegoObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tree";
    }
}
