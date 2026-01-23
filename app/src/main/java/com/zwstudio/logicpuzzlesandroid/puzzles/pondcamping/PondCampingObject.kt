package com.zwstudio.logicpuzzlesandroid.puzzles.pondcamping

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PondCampingObject {
    Empty, Forbidden, Marker, Hint, Forest
}

class PondCampingGameMove(val p: Position, var obj: PondCampingObject = PondCampingObject.Empty)
