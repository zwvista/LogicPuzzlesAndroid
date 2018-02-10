package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class WallSentinelsWallObject extends WallSentinelsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "wall";
    }
}
