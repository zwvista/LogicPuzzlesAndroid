package com.zwstudio.logicpuzzlesandroid.puzzles.lakesandmeadows

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LakesAndMeadowsObject {
    Empty, Hole, Block
}

class LakesAndMeadowsGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
