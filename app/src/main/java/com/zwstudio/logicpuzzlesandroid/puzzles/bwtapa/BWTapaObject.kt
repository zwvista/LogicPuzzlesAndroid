package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BWTapaObject {
    Empty, Hint, Marker, Wall
}

class BWTapaGameMove(val p: Position, var obj: BWTapaObject = BWTapaObject.Empty)
