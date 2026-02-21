package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SnakeMazeObject {
    Normal, Forbidden, Marker, Shaded;
    val isShaded: Boolean get() = this == Shaded
}

data class SnakeMazeHint(val num: Int, val dir: Int)

class SnakeMazeGameMove(val p: Position, var obj: SnakeMazeObject = SnakeMazeObject.Normal)
