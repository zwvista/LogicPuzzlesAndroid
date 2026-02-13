package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MirrorsObject {
    Empty, Block,
    UpRight, DownRight, LeftDown, LeftUp, Horizontal, Vertical
}

class MirrorsGameMove(val p: Position, var obj: MirrorsObject = MirrorsObject.Empty)
