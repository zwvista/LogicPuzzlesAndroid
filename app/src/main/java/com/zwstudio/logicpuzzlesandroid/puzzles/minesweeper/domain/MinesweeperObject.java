package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper.domain;

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
