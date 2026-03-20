package com.zwstudio.logicpuzzlesandroid.puzzles.lighthouses

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LighthousesObject {
    Empty, Forbidden, Hint, Marker, Lighthouse
}

class LighthousesGameMove(val p: Position, var obj: LighthousesObject = LighthousesObject.Empty)
