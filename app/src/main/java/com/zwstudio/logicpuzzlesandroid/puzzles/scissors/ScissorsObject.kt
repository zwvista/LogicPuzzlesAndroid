package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ScissorsObject {
    Empty, Forward, Backward
}

class ScissorsGameMove(val p: Position, var obj: ScissorsObject = ScissorsObject.Empty)
