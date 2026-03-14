package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TapARowObject {
    Empty, Hint, Marker, Wall
}

class TapARowGameMove(val p: Position, var obj: TapARowObject = TapARowObject.Empty)
