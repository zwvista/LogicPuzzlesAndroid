package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class LitsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> LitsMarkerObject
            "tree" -> LitsTreeObject()
            else -> LitsEmptyObject
        }
    }
}

object LitsEmptyObject : LitsObject()

object LitsForbiddenObject : LitsObject() {
    override fun objAsString() = "forbidden"
}

object LitsMarkerObject : LitsObject() {
    override fun objAsString() = "marker"
}

class LitsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : LitsObject() {
    override fun objAsString() = "tree"
}

class LitsGameMove(val p: Position, var obj: LitsObject = LitsEmptyObject)
