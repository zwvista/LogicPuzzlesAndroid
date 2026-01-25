package com.zwstudio.logicpuzzlesandroid.puzzles.suspendedgravity

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class SuspendedGravityObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> SuspendedGravityMarkerObject
            "block" -> SuspendedGravityBlockObject()
            else -> SuspendedGravityEmptyObject
        }
    }
}

object SuspendedGravityEmptyObject : SuspendedGravityObject()

object SuspendedGravityMarkerObject : SuspendedGravityObject() {
    override fun objAsString() = "marker"
}

object SuspendedGravityForbiddenObject : SuspendedGravityObject() {
    override fun objAsString() = "forbidden"
}

class SuspendedGravityBlockObject(var state: AllowedObjectState = AllowedObjectState.Normal) : SuspendedGravityObject() {
    override fun objAsString() = "block"
}

class SuspendedGravityGameMove(val p: Position, var obj: SuspendedGravityObject = SuspendedGravityEmptyObject)
