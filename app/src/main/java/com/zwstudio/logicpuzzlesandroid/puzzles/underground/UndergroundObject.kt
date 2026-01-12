package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class UndergroundObject {
    Empty, Marker, Forbidden, Up, Right, Down, Left
}

class UndergroundGameMove(val p: Position, var obj: UndergroundObject = UndergroundObject.Empty)
