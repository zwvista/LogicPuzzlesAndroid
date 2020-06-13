package com.zwstudio.logicpuzzlesandroid.puzzles.tapalike

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

sealed class TapAlikeObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapAlikeMarkerObject()
            "wall" -> TapAlikeWallObject()
            else -> TapAlikeEmptyObject()
        }
    }
}

class TapAlikeEmptyObject : TapAlikeObject()

class TapAlikeHintObject(var state: HintState = HintState.Normal) : TapAlikeObject() {
    override fun objTypeAsString() = "hint"
}

class TapAlikeMarkerObject : TapAlikeObject() {
    override fun objTypeAsString() = "marker"
}

class TapAlikeWallObject(var state: HintState = HintState.Normal) : TapAlikeObject() {
    override fun objTypeAsString() = "wall"
}

class TapAlikeGameMove(val p: Position, var obj: TapAlikeObject = TapAlikeEmptyObject())
