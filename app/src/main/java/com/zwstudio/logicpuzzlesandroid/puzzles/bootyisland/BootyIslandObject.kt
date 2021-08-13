package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position


sealed class BootyIslandObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> BootyIslandMarkerObject
            "treasure" -> BootyIslandTreasureObject()
            else -> BootyIslandEmptyObject
        }
    }
}

object BootyIslandEmptyObject : BootyIslandObject()

object BootyIslandForbiddenObject : BootyIslandObject() {
    override fun objAsString() = "forbidden"
}

class BootyIslandHintObject(var state: HintState = HintState.Normal) : BootyIslandObject() {
    override fun objAsString() = "hint"
}

object BootyIslandMarkerObject : BootyIslandObject() {
    override fun objAsString() = "marker"
}

class BootyIslandTreasureObject(var state: AllowedObjectState = AllowedObjectState.Normal) : BootyIslandObject() {
    override fun objAsString() = "treasure"
}

class BootyIslandGameMove(val p: Position, var obj: BootyIslandObject = BootyIslandEmptyObject)
