package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain

abstract class CalcudokuHint {
    var op = ' '
    var result = 0
    override fun toString(): String {
        return (if (result == 0) "" else result.toString()) + op.toString()
    }
}