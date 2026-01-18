package com.zwstudio.logicpuzzlesandroid.puzzles.funnynumbers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class FunnyNumbersObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> FunnyNumbersMarkerObject
            "water" -> FunnyNumbersWaterObject()
            else -> FunnyNumbersEmptyObject
        }
    }
}

object FunnyNumbersEmptyObject : FunnyNumbersObject()

object FunnyNumbersForbiddenObject : FunnyNumbersObject() {
    override fun objAsString() = "forbidden"
}

object FunnyNumbersMarkerObject : FunnyNumbersObject() {
    override fun objAsString() = "marker"
}

class FunnyNumbersWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : FunnyNumbersObject() {
    override fun objAsString() = "water"
}

class FunnyNumbersGameMove(val p: Position, var obj: FunnyNumbersObject = FunnyNumbersEmptyObject)
