package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class FussyWaiterObject(var food: Char = ' ', var drink: Char = ' ')

class FussyWaiterGameMove(val p: Position, var obj: Char = ' ') {
    val isDrink get() = obj.isUpperCase()
}
