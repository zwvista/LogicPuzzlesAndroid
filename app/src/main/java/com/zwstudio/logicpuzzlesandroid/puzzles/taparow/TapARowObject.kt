package com.zwstudio.logicpuzzlesandroid.puzzles.taparow

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TapARowObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapARowMarkerObject
            "wall" -> TapARowWallObject()
            else -> TapARowEmptyObject
        }
    }
}

object TapARowEmptyObject : TapARowObject()

class TapARowHintObject(var state: HintState = HintState.Normal) : TapARowObject() {
    override fun objTypeAsString() = "hint"
}

object TapARowMarkerObject : TapARowObject() {
    override fun objTypeAsString() = "marker"
}

class TapARowWallObject(var state: HintState = HintState.Normal) : TapARowObject() {
    override fun objTypeAsString() = "wall"
}

class TapARowGameMove(val p: Position, var obj: TapARowObject = TapARowEmptyObject)
