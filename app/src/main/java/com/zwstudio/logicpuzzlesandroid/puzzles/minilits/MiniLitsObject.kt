package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MiniLitsObject {
    Empty, Forbidden, Marker, Tree
}

class MiniLitsGameMove(val p: Position, var obj: MiniLitsObject = MiniLitsObject.Empty)
