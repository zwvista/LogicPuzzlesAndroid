package com.zwstudio.logicpuzzlesandroid.puzzles.hedgemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HedgeMazeObject {
    Empty, Gate, Step, Fountain, Forbidden, Marker, Hedge
}

class HedgeMazeGameMove(val p: Position, var obj: HedgeMazeObject = HedgeMazeObject.Empty)
