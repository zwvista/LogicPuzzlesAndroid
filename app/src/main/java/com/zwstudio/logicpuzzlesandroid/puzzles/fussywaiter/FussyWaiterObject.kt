package com.zwstudio.logicpuzzlesandroid.puzzles.fussywaiter

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class FussyWaiterObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> FussyWaiterMarkerObject
            "flower" -> FussyWaiterFlowerObject()
            else -> FussyWaiterEmptyObject
        }
    }
}

object FussyWaiterBlockObject : FussyWaiterObject() {
    override fun objAsString() = "block"
}

object FussyWaiterEmptyObject : FussyWaiterObject()


object FussyWaiterForbiddenObject : FussyWaiterObject() {
    override fun objAsString() = "forbidden"
}

object FussyWaiterMarkerObject : FussyWaiterObject() {
    override fun objAsString() = "marker"
}

class FussyWaiterFlowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : FussyWaiterObject() {
    override fun objAsString() = "flower"
}

class FussyWaiterGameMove(val p: Position, var obj: FussyWaiterObject = FussyWaiterEmptyObject)
