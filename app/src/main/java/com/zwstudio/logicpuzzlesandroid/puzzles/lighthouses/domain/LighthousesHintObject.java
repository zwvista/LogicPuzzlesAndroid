package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class LighthousesHintObject extends LighthousesObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
