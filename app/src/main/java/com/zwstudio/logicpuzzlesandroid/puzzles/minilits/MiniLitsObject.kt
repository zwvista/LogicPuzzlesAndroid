package com.zwstudio.logicpuzzlesandroid.puzzles.minilits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class MiniLitsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> MiniLitsMarkerObject
            "tree" -> MiniLitsTreeObject()
            else -> MiniLitsEmptyObject
        }
    }
}

object MiniLitsEmptyObject : MiniLitsObject()

object MiniLitsForbiddenObject : MiniLitsObject() {
    override fun objAsString() = "forbidden"
}

object MiniLitsMarkerObject : MiniLitsObject() {
    override fun objAsString() = "marker"
}

class MiniLitsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : MiniLitsObject() {
    override fun objAsString() = "tree"
}

class MiniLitsGameMove(val p: Position, var obj: MiniLitsObject = MiniLitsEmptyObject)
