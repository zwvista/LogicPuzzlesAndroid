package com.zwstudio.logicpuzzlesandroid.puzzles.orchards

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class OrchardsObject {
    Empty, Forbidden, Marker, Tree
}

class OrchardsGameMove(val p: Position, var obj: OrchardsObject = OrchardsObject.Empty)
