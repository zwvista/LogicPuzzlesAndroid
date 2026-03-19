package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class GardenerObject {
    Empty, Forbidden, Marker, Flower
}

class GardenerGameMove(val p: Position, var obj: GardenerObject = GardenerObject.Empty)
