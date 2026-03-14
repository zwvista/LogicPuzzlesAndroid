package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BalancedTapasObject {
    Empty, Hint, Marker, Wall
}

class BalancedTapasGameMove(val p: Position, var obj: BalancedTapasObject = BalancedTapasObject.Empty)
