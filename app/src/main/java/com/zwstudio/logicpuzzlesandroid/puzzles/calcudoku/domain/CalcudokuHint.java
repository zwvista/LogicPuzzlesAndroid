package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain;

public abstract class CalcudokuHint {
    public char op = ' ';
    public int result = 0;
    public String toString() {
        return (result == 0 ? "" : String.valueOf(result)) + String.valueOf(op);
    }
}
