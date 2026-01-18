package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class JoinMeObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> JoinMeMarkerObject
            "water" -> JoinMeWaterObject()
            else -> JoinMeEmptyObject
        }
    }
}

object JoinMeEmptyObject : JoinMeObject()

object JoinMeForbiddenObject : JoinMeObject() {
    override fun objAsString() = "forbidden"
}

object JoinMeMarkerObject : JoinMeObject() {
    override fun objAsString() = "marker"
}

class JoinMeWaterObject(var state: AllowedObjectState = AllowedObjectState.Normal) : JoinMeObject() {
    override fun objAsString() = "water"
}

class JoinMeGameMove(val p: Position, var obj: JoinMeObject = JoinMeEmptyObject)
