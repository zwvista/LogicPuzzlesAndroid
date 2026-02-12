package com.zwstudio.logicpuzzlesandroid.puzzles.tetrominopegs

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

data class TetrominoPegsObject(val rng: List<Position>, val kind: Int)

class TetrominoPegsGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
