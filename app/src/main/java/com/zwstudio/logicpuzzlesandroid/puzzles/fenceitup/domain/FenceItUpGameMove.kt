package com.zwstudio.logicpuzzlesandroid.puzzles.fenceitup.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

open class FenceItUpGameMove {
    var p: Position? = null
    var dir = 0
    var obj: GridLineObject? = null
}