package com.zwstudio.logicpuzzlesandroid.puzzles.underground

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class UndergroundObject {
    Empty, Marker, Block, Balloon, Weight
}

class UndergroundGameMove(val p: Position, var obj: UndergroundObject = UndergroundObject.Empty)
