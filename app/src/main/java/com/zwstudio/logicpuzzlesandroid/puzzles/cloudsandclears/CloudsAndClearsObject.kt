package com.zwstudio.logicpuzzlesandroid.puzzles.cloudsandclears

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CloudsAndClearsObject {
    Empty, Marker,
    Left, Right, Horizontal, Top, Bottom, Vertical;
    fun isCar() = this !in listOf(Empty, Marker)
}

class CloudsAndClearsGameMove(val p: Position, var obj: CloudsAndClearsObject = CloudsAndClearsObject.Empty)
