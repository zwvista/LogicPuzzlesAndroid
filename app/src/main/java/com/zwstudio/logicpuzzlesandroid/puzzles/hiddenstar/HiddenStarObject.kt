package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstar

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HiddenStarObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HiddenStarMarkerObject
            "tent" -> HiddenStarTentObject()
            else -> HiddenStarEmptyObject
        }
    }
}

object HiddenStarEmptyObject : HiddenStarObject()

object HiddenStarForbiddenObject : HiddenStarObject() {
    override fun objAsString() = "forbidden"
}

object HiddenStarMarkerObject : HiddenStarObject() {
    override fun objAsString() = "marker"
}

class HiddenStarTentObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarObject() {
    override fun objAsString() = "tent"
}

class HiddenStarTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarObject() {
    override fun objAsString() = "tree"
}

class HiddenStarGameMove(val p: Position, var obj: HiddenStarObject = HiddenStarEmptyObject)
