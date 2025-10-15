package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TurnTwiceObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TurnTwiceMarkerObject
            "flower" -> TurnTwiceFlowerObject()
            else -> TurnTwiceEmptyObject
        }
    }
}

object TurnTwiceBlockObject : TurnTwiceObject() {
    override fun objAsString() = "block"
}

object TurnTwiceEmptyObject : TurnTwiceObject()


object TurnTwiceForbiddenObject : TurnTwiceObject() {
    override fun objAsString() = "forbidden"
}

object TurnTwiceMarkerObject : TurnTwiceObject() {
    override fun objAsString() = "marker"
}

class TurnTwiceFlowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TurnTwiceObject() {
    override fun objAsString() = "flower"
}

class TurnTwiceGameMove(val p: Position, var obj: TurnTwiceObject = TurnTwiceEmptyObject)
