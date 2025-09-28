package com.zwstudio.logicpuzzlesandroid.puzzles.hiddenstars

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class HiddenStarsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HiddenStarsMarkerObject
            "tent" -> HiddenStarsTentObject()
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

class HiddenStarsTentObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarsObject() {
    override fun objAsString() = "tent"
}

class HiddenStarsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HiddenStarsObject() {
    override fun objAsString() = "tree"
}

class HiddenStarsGameMove(val p: Position, var obj: HiddenStarsObject = HiddenStarsEmptyObject)
