package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class NorthPoleFishingObject {
    Empty, Hole, Block
}

class NorthPoleFishingGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
