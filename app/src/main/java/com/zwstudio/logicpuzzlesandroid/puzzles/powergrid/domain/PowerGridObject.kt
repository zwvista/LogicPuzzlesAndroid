package com.zwstudio.logicpuzzlesandroid.puzzles.powergrid.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PowerGridObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> PowerGridMarkerObject()
            "tower" -> PowerGridPostObject()
            else -> PowerGridMarkerObject()
        }
    }
}

class PowerGridEmptyObject : PowerGridObject()

class PowerGridForbiddenObject : PowerGridObject() {
    override fun objAsString() = "forbidden"
}

class PowerGridMarkerObject : PowerGridObject() {
    override fun objAsString() = "marker"
}

class PowerGridPostObject(var state: AllowedObjectState = AllowedObjectState.Normal) : PowerGridObject() {
    override fun objAsString() = "tower"
}

class PowerGridGameMove(val p: Position, var obj: PowerGridObject = PowerGridEmptyObject())
