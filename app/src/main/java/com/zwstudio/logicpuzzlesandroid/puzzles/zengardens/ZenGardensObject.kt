package com.zwstudio.logicpuzzlesandroid.puzzles.zengardens

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ZenGardensObject {
    Empty, Stone, Leaf
}

class ZenGardensGameMove(val p: Position, var obj: ZenGardensObject = ZenGardensObject.Empty)
