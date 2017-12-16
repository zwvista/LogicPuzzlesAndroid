package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

public class SentinelsTowerObject extends SentinelsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tower";
    }
}
