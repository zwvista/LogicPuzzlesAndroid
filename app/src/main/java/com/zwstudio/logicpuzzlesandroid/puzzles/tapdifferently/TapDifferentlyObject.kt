package com.zwstudio.logicpuzzlesandroid.puzzles.tapdifferently

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TapDifferentlyObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapDifferentlyMarkerObject()
            "wall" -> TapDifferentlyWallObject()
            else -> TapDifferentlyEmptyObject()
        }
    }
}

class TapDifferentlyEmptyObject : TapDifferentlyObject()

class TapDifferentlyHintObject(var state: HintState = HintState.Normal) : TapDifferentlyObject() {
    override fun objTypeAsString() = "hint"
}

class TapDifferentlyMarkerObject : TapDifferentlyObject() {
    override fun objTypeAsString() = "marker"
}

class TapDifferentlyWallObject(var state: HintState = HintState.Normal) : TapDifferentlyObject() {
    override fun objTypeAsString() = "wall"
}

class TapDifferentlyGameMove(val p: Position, var obj: TapDifferentlyObject = TapDifferentlyEmptyObject())
