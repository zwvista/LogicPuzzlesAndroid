package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class DesertDunesObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "horz" -> DesertDunesHorzObject
            "vert" -> DesertDunesVertObject
            else -> DesertDunesEmptyObject
        }
    }
}

object DesertDunesEmptyObject : DesertDunesObject()

class DesertDunesHintObject(var state: HintState = HintState.Normal) : DesertDunesObject() {
    override fun objAsString() = "hint"
}

object DesertDunesHorzObject : DesertDunesObject() {
    override fun objAsString() = "horz"
}

object DesertDunesVertObject : DesertDunesObject() {
    override fun objAsString() = "vert"
}

class DesertDunesGameMove(val p: Position, var obj: DesertDunesObject = DesertDunesEmptyObject)
