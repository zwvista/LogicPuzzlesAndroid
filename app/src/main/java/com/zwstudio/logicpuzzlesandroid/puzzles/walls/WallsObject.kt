package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class WallsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "horz" -> WallsHorzObject
            "vert" -> WallsVertObject
            else -> WallsEmptyObject
        }
    }
}

object WallsEmptyObject : WallsObject()

class WallsHintObject(var walls: Int = 0, var state: HintState = HintState.Normal) : WallsObject() {
    override fun objAsString() = "hint"
}

object WallsHorzObject : WallsObject() {
    override fun objAsString() = "horz"
}

object WallsVertObject : WallsObject() {
    override fun objAsString() = "vert"
}

class WallsGameMove(val p: Position, var obj: WallsObject = WallsEmptyObject)
