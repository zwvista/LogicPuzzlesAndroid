package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.domain.Position


enum class BootyIslandObject {
    Empty, Forbidden, Hint, Marker, Treasure
}

class BootyIslandGameMove(val p: Position, var obj: BootyIslandObject = BootyIslandObject.Empty)
