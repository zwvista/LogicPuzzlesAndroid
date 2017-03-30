package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain;

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class SentinelsTowerObject extends SentinelsObject {
    public AllowedObjectState state = AllowedObjectState.Normal;
    public String objAsString() {
        return "tower";
    }
}
