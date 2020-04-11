package com.zwstudio.logicpuzzlesandroid.puzzles.gardener.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class GardenerObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> GardenerMarkerObject()
            "tree" -> GardenerTreeObject()
            else -> GardenerEmptyObject()
        }
    }
}

class GardenerEmptyObject : GardenerObject()

class GardenerForbiddenObject : GardenerObject() {
    override fun objAsString() = "forbidden"
}

class GardenerMarkerObject : GardenerObject() {
    override fun objAsString() = "marker"
}

class GardenerTreeObject(var state: HintState = HintState.Normal) : GardenerObject() {
    override fun objAsString() = "tree"
}

class GardenerGameMove(val p: Position, var obj: GardenerObject = GardenerEmptyObject())