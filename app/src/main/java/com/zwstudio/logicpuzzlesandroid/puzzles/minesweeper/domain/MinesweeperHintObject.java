package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class MinesweeperHintObject extends MinesweeperObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
