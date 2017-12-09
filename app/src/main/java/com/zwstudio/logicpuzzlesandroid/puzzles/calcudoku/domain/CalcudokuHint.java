package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public abstract class CalcudokuHint {
    public char op = ' ';
    public int result = 0;
    public String toString() {
        return (result == 0 ? "" : String.valueOf(result)) + String.valueOf(op);
    }
}
