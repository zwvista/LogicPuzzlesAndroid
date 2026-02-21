package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class UnreliableHintsObject {
    Normal, Forbidden, Marker, Shaded;
    val isShaded: Boolean get() = this == Shaded
}

data class UnreliableHintsHint(val num: Int, val dir: Int)

class UnreliableHintsGameMove(val p: Position, var obj: UnreliableHintsObject = UnreliableHintsObject.Normal)
