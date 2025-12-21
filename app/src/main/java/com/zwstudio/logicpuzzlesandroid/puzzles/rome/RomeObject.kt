package com.zwstudio.logicpuzzlesandroid.puzzles.rome

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class RomeObject {
    Empty, Rome, Up, Right, Down, Left
}

class RomeGameMove(val p: Position, var obj: RomeObject = RomeObject.Empty)
