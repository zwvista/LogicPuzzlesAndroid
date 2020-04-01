package com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DominoGameMove {
    var p: Position? = null
    var dir = 0
    var obj: GridLineObject? = null
}