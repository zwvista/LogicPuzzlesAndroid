package com.zwstudio.logicpuzzlesandroid.puzzles.turntwice

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TurnTwiceObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> TurnTwiceMarkerObject
            "signpost" -> TurnTwiceSignPostObject()
            "wall" -> TurnTwiceWallObject
            else -> TurnTwiceEmptyObject
        }
    }
}

object TurnTwiceEmptyObject : TurnTwiceObject()


object TurnTwiceForbiddenObject : TurnTwiceObject() {
    override fun objAsString() = "forbidden"
}

object TurnTwiceMarkerObject : TurnTwiceObject() {
    override fun objAsString() = "marker"
}

class TurnTwiceSignPostObject(var state: AllowedObjectState = AllowedObjectState.Normal) : TurnTwiceObject() {
    override fun objAsString() = "signpost"
}

object TurnTwiceWallObject : TurnTwiceObject() {
    override fun objAsString() = "wall"
}

class TurnTwiceGameMove(val p: Position, var obj: TurnTwiceObject = TurnTwiceEmptyObject)
