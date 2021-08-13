package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BWTapaObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str)  {
            "marker" -> BWTapaMarkerObject
            "wall" -> BWTapaWallObject()
            else -> BWTapaEmptyObject
        }
    }
}

object BWTapaEmptyObject : BWTapaObject()

class BWTapaHintObject(var state: HintState = HintState.Normal) : BWTapaObject() {
    override fun objTypeAsString() = "hint"
}

object BWTapaMarkerObject : BWTapaObject() {
    override fun objTypeAsString() = "marker"
}

class BWTapaWallObject(var state: HintState = HintState.Normal) : BWTapaObject() {
    override fun objTypeAsString() = "wall"
}

class BWTapaGameMove(val p: Position, var obj: BWTapaObject = BWTapaEmptyObject)
