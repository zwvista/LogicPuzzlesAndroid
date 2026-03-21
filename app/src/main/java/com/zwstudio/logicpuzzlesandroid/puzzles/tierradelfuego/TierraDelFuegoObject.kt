package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TierraDelFuegoObject {
    Empty, Forbidden, Hint, Marker, Water
}

class TierraDelFuegoGameMove(val p: Position, var obj: TierraDelFuegoObject = TierraDelFuegoObject.Empty)
