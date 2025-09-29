package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HiddenStarsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HiddenStarsMarkerObject
            "star" -> HiddenStarsStarObject()
            else -> HiddenStarsEmptyObject
        }
    }
}

object HiddenStarsEmptyObject : HiddenStarsObject()

object HiddenStarsForbiddenObject : HiddenStarsObject() {
    override fun objAsString() = "forbidden"
}

object HiddenStarsMarkerObject : HiddenStarsObject() {
    override fun objAsString() = "marker"
}

class HiddenStarsStarObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarsObject() {
    override fun objAsString() = "star"
}

class HiddenStarsArrowObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarsObject() {
    override fun objAsString() = "arrow"
}

class HiddenStarsGameMove(val p: Position, var obj: HiddenStarsObject = HiddenStarsEmptyObject)
