package com.zwstudio.logicpuzzlesandroid.puzzles.orchards.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class OrchardsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> OrchardsMarkerObject()
            "tree" -> OrchardsTreeObject()
            else -> OrchardsEmptyObject()
        }
    }
}

class OrchardsEmptyObject : OrchardsObject()

class OrchardsForbiddenObject : OrchardsObject() {
    override fun objAsString() = "forbidden"
}

class OrchardsMarkerObject : OrchardsObject() {
    override fun objAsString() = "marker"
}

class OrchardsTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : OrchardsObject() {
    override fun objAsString() = "tree"
}

class OrchardsGameMove(val p: Position, var obj: OrchardsObject = OrchardsEmptyObject())
