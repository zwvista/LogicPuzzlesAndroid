package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TurnTwiceObject {
    Empty, Forbidden, Marker, SignPost, Wall
}

class TurnTwiceGameMove(val p: Position, var obj: TurnTwiceObject = TurnTwiceObject.Empty)
