package com.zwstudio.logicpuzzlesandroid.puzzles.tents

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TentsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TentsMarkerObject
            "tent" -> TentsTentObject()
            else -> TentsEmptyObject
        }
    }
}

object TentsEmptyObject : TentsObject()

object TentsForbiddenObject : TentsObject() {
    override fun objAsString() = "forbidden"
}

object TentsMarkerObject : TentsObject() {
    override fun objAsString() = "marker"
}

class TentsTentObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TentsObject() {
    override fun objAsString() = "tent"
}

class TentsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TentsObject() {
    override fun objAsString() = "tree"
}

class TentsGameMove(val p: Position, var obj: TentsObject = TentsEmptyObject)
