package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class WallsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "horz" -> WallsHorzObject()
            "vert" -> WallsVertObject()
            else -> WallsEmptyObject()
        }
    }
}

class WallsEmptyObject : WallsObject()

class WallsHintObject(var walls: Int = 0, var state: HintState = HintState.Normal) : WallsObject() {
    override fun objAsString() = "hint"
}

class WallsHorzObject : WallsObject() {
    override fun objAsString() = "horz"
}

class WallsVertObject : WallsObject() {
    override fun objAsString() = "vert"
}

class WallsGameMove(val p: Position, var obj: WallsObject = WallsEmptyObject())
