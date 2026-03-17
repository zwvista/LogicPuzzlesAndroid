package com.zwstudio.logicpuzzlesandroid.puzzles.busyseas

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BusySeasObject {
    Empty, Forbidden, Hint, Marker, Lighthouse
}

class BusySeasGameMove(val p: Position, var obj: BusySeasObject = BusySeasObject.Empty)
