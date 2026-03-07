package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FourMeNotObject {
    Empty, Forbidden, Marker, Flower, Block
}

class FourMeNotGameMove(val p: Position, var obj: FourMeNotObject = FourMeNotObject.Empty)
