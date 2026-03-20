package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PairakabeObject {
    Empty, Hint, Marker, Wall
}

class PairakabeGameMove(val p: Position, var obj: PairakabeObject = PairakabeObject.Empty)
