package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class MinesweeperHintObject extends MinesweeperObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
