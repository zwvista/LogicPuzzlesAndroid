package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TapaObject {
    Empty, Hint, Marker, Wall
}

class TapaGameMove(val p: Position, var obj: TapaObject = TapaObject.Empty)
