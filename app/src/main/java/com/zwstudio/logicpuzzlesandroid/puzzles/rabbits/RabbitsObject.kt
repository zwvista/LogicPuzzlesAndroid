package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class RabbitsObject {
    Empty, Forbidden, Hint, Marker, Rabbit, Tree
}

class RabbitsGameMove(val p: Position, var obj: RabbitsObject = RabbitsObject.Empty)
