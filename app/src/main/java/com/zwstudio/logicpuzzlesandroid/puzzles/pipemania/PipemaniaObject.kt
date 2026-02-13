package com.zwstudio.logicpuzzlesandroid.puzzles.pipemania

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PipemaniaObject {
    Empty,
    UpRight, DownRight, LeftDown, LeftUp, Horizontal, Vertical, Cross
}

class PipemaniaGameMove(val p: Position, var obj: PipemaniaObject = PipemaniaObject.Empty)
