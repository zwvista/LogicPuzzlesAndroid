package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PataObject {
    Empty, Hint, Marker, Wall
}

class PataGameMove(val p: Position, var obj: PataObject = PataObject.Empty)
