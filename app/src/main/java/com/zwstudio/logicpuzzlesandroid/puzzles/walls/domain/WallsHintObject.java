package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class WallsHintObject extends WallsObject {
    public int walls;
    public HintState state;
    public String objAsString() {
        return "hint";
    }
}
