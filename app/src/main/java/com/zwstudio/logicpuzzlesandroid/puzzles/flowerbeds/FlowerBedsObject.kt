package com.zwstudio.logicpuzzlesandroid.puzzles.flowerbeds

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FlowerBedsObject {
    Empty, Flower, Hedge
}

data class FlowerBedsRect(val area: List<Position>, val rows: Int, val cols: Int)

class FlowerBedsGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
