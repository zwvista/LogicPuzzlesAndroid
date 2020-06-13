package com.zwstudio.logicpuzzlesandroid.puzzles.parks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class ParksObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> ParksMarkerObject()
            "tree" -> ParksTreeObject()
            else -> ParksEmptyObject()
        }
    }
}

class ParksEmptyObject : ParksObject() {
    override fun objAsString() = "empty"
}

class ParksForbiddenObject : ParksObject() {
    override fun objAsString() = "forbidden"
}

class ParksMarkerObject : ParksObject() {
    override fun objAsString() =  "marker"
}

class ParksTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : ParksObject() {
    override fun objAsString() = "tree"
}

class ParksGameMove(val p: Position, var obj: ParksObject = ParksEmptyObject())
