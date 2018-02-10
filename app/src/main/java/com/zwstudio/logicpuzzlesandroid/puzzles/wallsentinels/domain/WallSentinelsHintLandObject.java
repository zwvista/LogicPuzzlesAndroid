package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class WallSentinelsHintLandObject extends WallSentinelsObject {
    public int tiles;
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hintLand";
    }
}
