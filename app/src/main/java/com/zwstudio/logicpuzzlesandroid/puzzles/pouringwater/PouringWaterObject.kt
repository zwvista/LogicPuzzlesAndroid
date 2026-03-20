package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PouringWaterObject {
    Empty, Forbidden, Marker, Water
}

class PouringWaterGameMove(val p: Position, var obj: PouringWaterObject = PouringWaterObject.Empty)
