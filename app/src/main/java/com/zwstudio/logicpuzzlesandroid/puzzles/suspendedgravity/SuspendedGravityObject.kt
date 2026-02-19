package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SuspendedGravityObject {
    Empty, Forbidden, Marker,
    Stone
}

class SuspendedGravityGameMove(val p: Position, var obj: SuspendedGravityObject = SuspendedGravityObject.Empty)
