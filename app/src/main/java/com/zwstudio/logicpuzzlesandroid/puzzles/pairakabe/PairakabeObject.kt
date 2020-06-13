package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

sealed class PairakabeObject {
    open fun objAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "wall" -> PairakabeWallObject()
            "marker" -> PairakabeMarkerObject()
            else -> PairakabeEmptyObject()
        }
    }
}

class PairakabeEmptyObject : PairakabeObject()

class PairakabeHintObject(var state: HintState = HintState.Normal) : PairakabeObject() {
    override fun objAsString() = "hint"
}

class PairakabeMarkerObject : PairakabeObject() {
    override fun objAsString() = "marker"
}

class PairakabeWallObject : PairakabeObject() {
    override fun objAsString() = "wall"
}

class PairakabeGameMove(val p: Position, var obj: PairakabeObject = PairakabeEmptyObject())
