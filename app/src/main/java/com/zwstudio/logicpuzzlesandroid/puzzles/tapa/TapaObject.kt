package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TapaObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapaMarkerObject
            "wall" -> TapaWallObject()
            else -> TapaEmptyObject
        }
    }
}

object TapaEmptyObject : TapaObject()

class TapaHintObject(var state: HintState = HintState.Normal) : TapaObject() {
    override fun objTypeAsString() = "hint"
}

object TapaMarkerObject : TapaObject() {
    override fun objTypeAsString() = "marker"
}

class TapaWallObject(var state: HintState = HintState.Normal) : TapaObject() {
    override fun objTypeAsString() = "wall"
}

class TapaGameMove(val p: Position, var obj: TapaObject = TapaEmptyObject)
