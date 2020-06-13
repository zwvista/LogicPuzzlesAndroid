package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

sealed class TapaIslandsObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapaIslandsMarkerObject()
            "wall" -> TapaIslandsWallObject()
            else -> TapaIslandsEmptyObject()
        }
    }
}

class TapaIslandsEmptyObject : TapaIslandsObject()

class TapaIslandsHintObject(var state: HintState = HintState.Normal) : TapaIslandsObject() {
    override fun objTypeAsString() = "hint"
}

class TapaIslandsMarkerObject : TapaIslandsObject() {
    override fun objTypeAsString() = "marker"
}

class TapaIslandsWallObject(var state: HintState = HintState.Normal) : TapaIslandsObject() {
    override fun objTypeAsString() = "wall"
}

class TapaIslandsGameMove(val p: Position, var obj: TapaIslandsObject = TapaIslandsEmptyObject())
