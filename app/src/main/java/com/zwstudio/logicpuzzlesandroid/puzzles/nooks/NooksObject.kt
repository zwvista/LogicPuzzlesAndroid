package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class NooksObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "bread" -> NooksBreadObject()
            "ham" -> NooksHamObject()
            "marker" -> NooksMarkerObject
            else -> NooksMarkerObject
        }
    }
}

object NooksEmptyObject : NooksObject()

class NooksBreadObject(var state: AllowedObjectState = AllowedObjectState.Normal) : NooksObject() {
    override fun objAsString() = "bread"
}

object NooksForbiddenObject : NooksObject() {
    override fun objAsString() = "forbidden"
}

class NooksHamObject(var state: AllowedObjectState = AllowedObjectState.Normal) : NooksObject() {
    override fun objAsString() = "ham"
}

object NooksMarkerObject : NooksObject() {
    override fun objAsString() = "marker"
}

class NooksGameMove(val p: Position, var obj: NooksObject = NooksEmptyObject)
