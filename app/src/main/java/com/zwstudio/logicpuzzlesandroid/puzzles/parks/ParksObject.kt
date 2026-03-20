package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ParksObject {
    Empty, Forbidden, Marker, Tree
}

class ParksGameMove(val p: Position, var obj: ParksObject = ParksObject.Empty)
