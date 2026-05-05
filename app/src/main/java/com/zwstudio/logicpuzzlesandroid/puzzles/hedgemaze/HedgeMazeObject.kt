package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HedgeMazeObject {
    Empty, HedgeMaze, Up, Right, Down, Left
}

class HedgeMazeGameMove(val p: Position, var obj: HedgeMazeObject = HedgeMazeObject.Empty)
