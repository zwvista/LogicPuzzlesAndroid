package com.zwstudio.logicpuzzlesandroid.puzzles.mineslither

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MineSlitherObject {
    Empty, Forbidden, Marker, Mine
}

class MineSlitherGameMove(val p: Position, var obj: MineSlitherObject = MineSlitherObject.Empty)
