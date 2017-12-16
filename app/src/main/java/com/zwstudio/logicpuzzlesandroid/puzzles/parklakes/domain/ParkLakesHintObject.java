package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class ParkLakesHintObject extends ParkLakesObject {
    public HintState state = HintState.Normal;
    public int tiles;
    public String objAsString() {
        return "hint";
    }
}
