package com.zwstudio.logicpuzzlesandroid.puzzles.slantedmaze

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SlantedMazeObject {
    Empty, Forward, Backward
}

class SlantedMazeGameMove(val p: Position, var obj: SlantedMazeObject = SlantedMazeObject.Empty)
