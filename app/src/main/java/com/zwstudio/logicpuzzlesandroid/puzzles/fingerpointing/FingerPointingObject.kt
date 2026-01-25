package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FingerPointingObject {
    Empty, Block, Hint, Up, Right, Down, Left
}

class FingerPointingGameMove(val p: Position, var obj: FingerPointingObject = FingerPointingObject.Empty)
