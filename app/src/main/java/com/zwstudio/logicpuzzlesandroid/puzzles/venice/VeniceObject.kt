package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class VeniceObject {
    Empty, Hint, Marker, Water
}

class VeniceGameMove(val p: Position, var obj: VeniceObject = VeniceObject.Empty)
