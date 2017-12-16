package com.zwstudio.logicpuzzlesandroid.puzzles.bridges.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class BridgesIslandObject extends BridgesObject {
    public HintState state = HintState.Normal;
    public Integer[] bridges = {0, 0, 0, 0};
}
