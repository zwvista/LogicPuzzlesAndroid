package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SnakeMazeObject {
    Empty, Forbidden, Hint, Marker,
    Snake1, Snake2, Snake3, Snake4, Snake5;
    val isSnake: Boolean get() =
        listOf(Snake1, Snake2, Snake3, Snake4, Snake5).contains(this)
    val value: Int get() =
        if (isSnake) ordinal - Snake1.ordinal + 1 else 0
}

data class SnakeMazeHint(val num: Int, val dir: Int)

class SnakeMazeGameMove(val p: Position, var obj: SnakeMazeObject = SnakeMazeObject.Empty)
