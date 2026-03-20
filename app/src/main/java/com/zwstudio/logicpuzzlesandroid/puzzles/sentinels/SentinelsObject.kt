package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SentinelsObject {
    Empty, Forbidden, Hint, Marker, Tower
}

class SentinelsGameMove(val p: Position, var obj: SentinelsObject = SentinelsObject.Empty)
