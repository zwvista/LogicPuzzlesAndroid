package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TapAlikeObject {
    Empty, Hint, Marker, Wall
}

class TapAlikeGameMove(val p: Position, var obj: TapAlikeObject = TapAlikeObject.Empty)
