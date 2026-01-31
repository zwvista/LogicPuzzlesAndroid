package com.zwstudio.logicpuzzlesandroid.puzzles.newcarpentersquare

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class NewCarpenterSquareHint {
    Equal, NotEqual, Unknown
}

class NewCarpenterSquareGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
