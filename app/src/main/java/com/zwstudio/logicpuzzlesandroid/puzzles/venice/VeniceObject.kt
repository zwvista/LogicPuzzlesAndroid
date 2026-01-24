package com.zwstudio.logicpuzzlesandroid.puzzles.venice

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class VeniceObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> VeniceMarkerObject
            "tower" -> VeniceTowerObject()
            else -> VeniceMarkerObject
        }
    }
}

object VeniceEmptyObject : VeniceObject()

object VeniceForbiddenObject : VeniceObject() {
    override fun objAsString() = "forbidden"
}

class VeniceHintObject(var state: HintState = HintState.Normal) : VeniceObject() {
    override fun objAsString() = "hint"
}

object VeniceMarkerObject : VeniceObject() {
    override fun objAsString() = "marker"
}

class VeniceTowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : VeniceObject() {
    override fun objAsString() = "tower"
}

class VeniceGameMove(val p: Position, var obj: VeniceObject = VeniceEmptyObject)
