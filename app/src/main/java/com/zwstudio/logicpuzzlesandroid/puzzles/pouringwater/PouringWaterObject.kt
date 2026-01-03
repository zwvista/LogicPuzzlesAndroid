package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PouringWaterObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> PouringWaterMarkerObject
            "water" -> PouringWaterWaterObject()
            else -> PouringWaterEmptyObject
        }
    }
}

object PouringWaterEmptyObject : PouringWaterObject()

object PouringWaterForbiddenObject : PouringWaterObject() {
    override fun objAsString() = "forbidden"
}

object PouringWaterMarkerObject : PouringWaterObject() {
    override fun objAsString() = "marker"
}

class PouringWaterWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : PouringWaterObject() {
    override fun objAsString() = "water"
}

class PouringWaterGameMove(val p: Position, var obj: PouringWaterObject = PouringWaterEmptyObject)
