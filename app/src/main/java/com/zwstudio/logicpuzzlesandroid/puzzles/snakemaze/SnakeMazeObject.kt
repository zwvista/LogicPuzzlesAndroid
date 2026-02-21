package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class SnakeMazeHint(val num: Int, val dir: Int)

class SnakeMazeGameMove(val p: Position, var dir: Int = 0)
