package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ChocolateObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ChocolateMarkerObject
            "chocolate" -> ChocolateChocolateObject()
            else -> ChocolateEmptyObject
        }
    }
}

object ChocolateEmptyObject : ChocolateObject()

object ChocolateMarkerObject : ChocolateObject() {
    override fun objAsString() = "marker"
}

object ChocolateForbiddenObject : ChocolateObject() {
    override fun objAsString() = "forbidden"
}

class ChocolateChocolateObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ChocolateObject() {
    override fun objAsString() = "chocolate"
}

class ChocolateGameMove(val p: Position, var obj: ChocolateObject = ChocolateEmptyObject)
