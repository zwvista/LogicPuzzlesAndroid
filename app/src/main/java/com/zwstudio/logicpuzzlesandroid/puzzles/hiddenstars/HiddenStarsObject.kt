package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HiddenStarsObject {
    Empty, Forbidden, Marker, Star, Arrow
}

class HiddenStarsGameMove(val p: Position, var obj: HiddenStarsObject = HiddenStarsObject.Empty)
