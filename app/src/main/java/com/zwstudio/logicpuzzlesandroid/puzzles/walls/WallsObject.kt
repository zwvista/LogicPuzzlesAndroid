package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class WallsObject {
    Empty, Hint, Horz, Vert
}

class WallsGameMove(val p: Position, var obj: WallsObject = WallsObject.Empty)
