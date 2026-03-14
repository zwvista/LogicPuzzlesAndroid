package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TapaIslandsObject {
    Empty, Hint, Marker, Wall
}

class TapaIslandsGameMove(val p: Position, var obj: TapaIslandsObject = TapaIslandsObject.Empty)
