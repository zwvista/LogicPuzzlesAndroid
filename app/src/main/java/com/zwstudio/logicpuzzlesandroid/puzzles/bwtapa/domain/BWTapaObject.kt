package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class BWTapaObject {
    open fun objTypeAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str)  {
            "marker" -> BWTapaMarkerObject()
            "wall" -> BWTapaWallObject()
            else -> BWTapaEmptyObject()
        }
    }
}

class BWTapaEmptyObject : BWTapaObject()

class BWTapaHintObject(var state: HintState = HintState.Normal) : BWTapaObject() {
    override fun objTypeAsString() = "hint"
}

class BWTapaMarkerObject : BWTapaObject() {
    override fun objTypeAsString() = "marker"
}

class BWTapaWallObject(var state: HintState = HintState.Normal) : BWTapaObject() {
    override fun objTypeAsString() = "wall"
}

class BWTapaGameMove(val p: Position, var obj: BWTapaObject = BWTapaEmptyObject())
