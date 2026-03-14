package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TapDifferentlyObject {
    Empty, Hint, Marker, Wall
}

class TapDifferentlyGameMove(val p: Position, var obj: TapDifferentlyObject = TapDifferentlyObject.Empty)
