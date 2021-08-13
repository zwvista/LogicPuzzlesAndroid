package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class TapaIslandsObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "marker" -> TapaIslandsMarkerObject
            "wall" -> TapaIslandsWallObject()
            else -> TapaIslandsEmptyObject
        }
    }
}

object TapaIslandsEmptyObject : TapaIslandsObject()

class TapaIslandsHintObject(var state: HintState = HintState.Normal) : TapaIslandsObject() {
    override fun objTypeAsString() = "hint"
}

object TapaIslandsMarkerObject : TapaIslandsObject() {
    override fun objTypeAsString() = "marker"
}

class TapaIslandsWallObject(var state: HintState = HintState.Normal) : TapaIslandsObject() {
    override fun objTypeAsString() = "wall"
}

class TapaIslandsGameMove(val p: Position, var obj: TapaIslandsObject = TapaIslandsEmptyObject)
