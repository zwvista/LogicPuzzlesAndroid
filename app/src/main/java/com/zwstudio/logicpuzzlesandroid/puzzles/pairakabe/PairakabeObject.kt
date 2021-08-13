package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PairakabeObject {
    open fun objAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "wall" -> PairakabeWallObject
            "marker" -> PairakabeMarkerObject
            else -> PairakabeEmptyObject
        }
    }
}

object PairakabeEmptyObject : PairakabeObject()

class PairakabeHintObject(var state: HintState = HintState.Normal) : PairakabeObject() {
    override fun objAsString() = "hint"
}

object PairakabeMarkerObject : PairakabeObject() {
    override fun objAsString() = "marker"
}

object PairakabeWallObject : PairakabeObject() {
    override fun objAsString() = "wall"
}

class PairakabeGameMove(val p: Position, var obj: PairakabeObject = PairakabeEmptyObject)
