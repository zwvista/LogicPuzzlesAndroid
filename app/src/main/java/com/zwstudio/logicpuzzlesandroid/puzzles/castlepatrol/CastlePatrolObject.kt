package com.zwstudio.logicpuzzlesandroid.puzzles.castlepatrol

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CastlePatrolObject(var lightness: Int = 0) {
    Empty, Marker, Wall, EmptyHint, WallHint;
    fun isHint() = this == EmptyHint || this == WallHint
}

class CastlePatrolGameMove(val p: Position, var obj: CastlePatrolObject = CastlePatrolObject.Empty)
