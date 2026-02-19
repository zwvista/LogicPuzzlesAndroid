package com.zwstudio.logicpuzzlesandroid.puzzles.unreliablehints

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class UnreliableHintsObject {
    Normal, Darken, Marker
}

class UnreliableHintsGameMove(val p: Position, var obj: UnreliableHintsObject = UnreliableHintsObject.Normal)
