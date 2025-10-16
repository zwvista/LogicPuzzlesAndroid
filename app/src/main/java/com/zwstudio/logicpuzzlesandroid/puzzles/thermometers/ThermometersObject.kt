package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ThermometersObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ThermometersMarkerObject
            "star" -> ThermometersStarObject()
            else -> ThermometersEmptyObject
        }
    }
}

object ThermometersEmptyObject : ThermometersObject()

object ThermometersForbiddenObject : ThermometersObject() {
    override fun objAsString() = "forbidden"
}

object ThermometersMarkerObject : ThermometersObject() {
    override fun objAsString() = "marker"
}

class ThermometersStarObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ThermometersObject() {
    override fun objAsString() = "star"
}

class ThermometersArrowObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ThermometersObject() {
    override fun objAsString() = "arrow"
}

class ThermometersGameMove(val p: Position, var obj: ThermometersObject = ThermometersEmptyObject)
