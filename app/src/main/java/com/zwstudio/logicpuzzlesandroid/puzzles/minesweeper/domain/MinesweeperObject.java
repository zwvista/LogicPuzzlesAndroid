package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class MinesweeperObject {
    public abstract String objAsString();
    public static MinesweeperObject objFromString(String str) {
        switch (str) {
        case "mine":
            return new MinesweeperMineObject();
        case "marker":
            return new MinesweeperMarkerObject();
        default:
            return new MinesweeperEmptyObject();
        }
    }
}
