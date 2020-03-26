package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

open class CarpentersSquareGameMove {
    var p: Position? = null
    var dir = 0
    var obj: GridLineObject? = null
}