package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class FourMeNotObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> FourMeNotMarkerObject
            "tree" -> FourMeNotTreeObject()
            else -> FourMeNotEmptyObject
        }
    }
}

object FourMeNotBlockObject : FourMeNotObject() {
    override fun objAsString() = "block"
}

object FourMeNotEmptyObject : FourMeNotObject()


object FourMeNotForbiddenObject : FourMeNotObject() {
    override fun objAsString() = "forbidden"
}

object FourMeNotMarkerObject : FourMeNotObject() {
    override fun objAsString() = "marker"
}

class FourMeNotTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : FourMeNotObject() {
    override fun objAsString() = "tree"
}

class FourMeNotGameMove(val p: Position, var obj: FourMeNotObject = FourMeNotEmptyObject)
