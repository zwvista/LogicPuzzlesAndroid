package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domain;

public abstract class MathraxHint {
    public char op = ' ';
    public int result = 0;
    public String toString() {
        return (result == 0 ? "" : String.valueOf(result)) + String.valueOf(op);
    }
}
