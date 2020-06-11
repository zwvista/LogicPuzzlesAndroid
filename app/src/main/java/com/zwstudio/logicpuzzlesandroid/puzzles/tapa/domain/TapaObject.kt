package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class TapaObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapaMarkerObject()
            "wall" -> TapaWallObject()
            else -> TapaEmptyObject()
        }
    }
}

class TapaEmptyObject : TapaObject()

class TapaHintObject(var state: HintState = HintState.Normal) : TapaObject() {
    override fun objTypeAsString() = "hint"
}

class TapaMarkerObject : TapaObject() {
    override fun objTypeAsString() = "marker"
}

class TapaWallObject(var state: HintState = HintState.Normal) : TapaObject() {
    override fun objTypeAsString() = "wall"
}

class TapaGameMove(val p: Position, var obj: TapaObject = TapaEmptyObject())
