package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class FingerPointingObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "mine" -> FingerPointingMineObject
            "marker" -> FingerPointingMarkerObject
            else -> FingerPointingEmptyObject
        }
    }
}

object FingerPointingEmptyObject : FingerPointingObject()

object FingerPointingForbiddenObject : FingerPointingObject() {
    override fun objAsString() = "forbidden"
}

class FingerPointingHintObject(var state: HintState = HintState.Normal) : FingerPointingObject() {
    override fun objAsString() = "hint"
}

object FingerPointingMarkerObject : FingerPointingObject() {
    override fun objAsString() = "marker"
}

object FingerPointingMineObject : FingerPointingObject() {
    override fun objAsString() = "mine"
}

class FingerPointingGameMove(val p: Position, var obj: FingerPointingObject = FingerPointingEmptyObject)
