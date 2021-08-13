package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class NurikabeObject {
    open fun objAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "wall" -> NurikabeWallObject
            "marker" -> NurikabeMarkerObject
            else -> NurikabeEmptyObject
        }
    }
}

object NurikabeEmptyObject : NurikabeObject()

class NurikabeHintObject(var state: HintState = HintState.Normal) : NurikabeObject() {
    override fun objAsString() = "hint"
}

object NurikabeMarkerObject : NurikabeObject() {
    override fun objAsString() = "marker"
}

object NurikabeWallObject : NurikabeObject() {
    override fun objAsString() = "wall"
}

class NurikabeGameMove(val p: Position, var obj: NurikabeObject = NurikabeEmptyObject)
