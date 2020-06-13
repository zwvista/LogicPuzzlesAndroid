package com.zwstudio.logicpuzzlesandroid.puzzles.pata

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class PataObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> PataMarkerObject()
            "wall" -> PataWallObject()
            else -> PataEmptyObject()
        }
    }
}

class PataEmptyObject : PataObject()

class PataHintObject(var state: HintState = HintState.Normal) : PataObject() {
    override fun objTypeAsString() = "hint"
}

class PataMarkerObject : PataObject() {
    override fun objTypeAsString() = "marker"
}

class PataWallObject(var state: HintState = HintState.Normal) : PataObject() {
    override fun objTypeAsString() = "wall"
}

class PataGameMove(val p: Position, var obj: PataObject = PataEmptyObject())
