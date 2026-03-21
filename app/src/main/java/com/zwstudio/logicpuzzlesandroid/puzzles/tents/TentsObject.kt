package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TentsObject {
    Empty, Forbidden, Hint, Marker, Tent, Tree
}

class TentsGameMove(val p: Position, var obj: TentsObject = TentsObject.Empty)
