package com.zwstudio.logicpuzzlesandroid.puzzles.floweromino

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FlowerOMinoObject {
    Empty, Flower, Hedge
}

data class FlowerOMinoRect(val area: List<Position>, val rows: Int, val cols: Int)

class FlowerOMinoGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
