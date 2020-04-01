package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LoopyGameMove {
    var p: Position? = null
    var dir = 0
    var obj: GridLineObject? = null
}