package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MathraxHint(var op: Char = ' ', var result: Int = 0) {
    override fun toString() = (if (result == 0) "" else result.toString()) + op.toString()
}

class MathraxGameMove(val p: Position, var obj: Int = 0)
