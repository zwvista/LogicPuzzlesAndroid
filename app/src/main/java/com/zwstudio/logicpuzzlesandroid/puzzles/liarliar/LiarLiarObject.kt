package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class LiarLiarObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> LiarLiarMarkerObject
            "marked" -> LiarLiarMarkedObject
            else -> LiarLiarEmptyObject
        }
    }
}

object LiarLiarEmptyObject : LiarLiarObject()

class LiarLiarHintObject(var state: HintState = HintState.Normal) : LiarLiarObject() {
    override fun objAsString() = "hint"
}

object LiarLiarMarkerObject : LiarLiarObject() {
    override fun objAsString() = "marker"
}

object LiarLiarMarkedObject : LiarLiarObject() {
    override fun objAsString() = "marked"
}

class LiarLiarGameMove(val p: Position, var obj: LiarLiarObject = LiarLiarEmptyObject)
