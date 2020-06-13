package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CastleBaileyObject {
    Empty, Forbidden, Marker, Wall
}

class CastleBaileyGameMove(val p: Position, var obj: CastleBaileyObject = CastleBaileyObject.Empty)
