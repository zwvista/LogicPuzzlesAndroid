package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LiarLiarObject {
    Empty, Forbidden, Hint, Marker, Marked
}

class LiarLiarGameMove(val p: Position, var obj: LiarLiarObject = LiarLiarObject.Empty)
