package com.zwstudio.logicpuzzlesandroid.puzzles.lits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class LitsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> LitsMarkerObject()
            "tree" -> LitsTreeObject()
            else -> LitsEmptyObject()
        }
    }
}

class LitsEmptyObject : LitsObject()

class LitsForbiddenObject : LitsObject() {
    override fun objAsString() = "forbidden"
}

class LitsMarkerObject : LitsObject() {
    override fun objAsString() = "marker"
}

class LitsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : LitsObject() {
    override fun objAsString() = "tree"
}

class LitsGameMove(val p: Position, var obj: LitsObject = LitsEmptyObject())
