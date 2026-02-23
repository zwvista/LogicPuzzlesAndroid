package com.zwstudio.logicpuzzlesandroid.puzzles.snakeomino

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SnakeominoObject {
    Empty, Flower, Hedge
}

data class SnakeominoRect(val area: List<Position>, val rows: Int, val cols: Int)

class SnakeominoGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
