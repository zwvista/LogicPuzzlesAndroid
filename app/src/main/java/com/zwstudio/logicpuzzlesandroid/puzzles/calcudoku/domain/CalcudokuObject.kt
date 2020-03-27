package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CalcudokuGameMove(val p: Position, var obj: Int = 0)

class CalcudokuHint(var op: Char = ' ', var result: Int = 0) {
    override fun toString() = (if (result == 0) "" else result.toString()) + op.toString()
}
