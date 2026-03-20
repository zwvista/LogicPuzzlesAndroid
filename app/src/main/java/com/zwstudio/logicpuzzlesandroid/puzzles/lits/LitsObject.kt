package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LitsObject {
    Empty, Forbidden, Marker, Tree
}

class LitsGameMove(val p: Position, var obj: LitsObject = LitsObject.Empty)
