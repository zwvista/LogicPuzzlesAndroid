package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

sealed class NurikabeObject {
    open fun objAsString() = "empty"

    companion object {
        fun objTypeFromString(str: String) = when (str) {
            "wall" -> NurikabeWallObject()
            "marker" -> NurikabeMarkerObject()
            else -> NurikabeEmptyObject()
        }
    }
}

class NurikabeEmptyObject : NurikabeObject()

class NurikabeHintObject(var state: HintState = HintState.Normal) : NurikabeObject() {
    override fun objAsString() = "hint"
}

class NurikabeMarkerObject : NurikabeObject() {
    override fun objAsString() = "marker"
}

class NurikabeWallObject : NurikabeObject() {
    override fun objAsString() = "wall"
}

class NurikabeGameMove(val p: Position, var obj: NurikabeObject = NurikabeEmptyObject())
