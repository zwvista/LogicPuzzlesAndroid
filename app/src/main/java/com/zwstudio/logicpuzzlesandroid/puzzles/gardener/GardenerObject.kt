package com.zwstudio.logicpuzzlesandroid.puzzles.gardener

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class GardenerObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> GardenerMarkerObject
            "flower" -> GardenerFlowerObject()
            else -> GardenerEmptyObject
        }
    }
}

object GardenerEmptyObject : GardenerObject()

object GardenerForbiddenObject : GardenerObject() {
    override fun objAsString() = "forbidden"
}

object GardenerMarkerObject : GardenerObject() {
    override fun objAsString() = "marker"
}

class GardenerFlowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : GardenerObject() {
    override fun objAsString() = "flower"
}

class GardenerGameMove(val p: Position, var obj: GardenerObject = GardenerEmptyObject)
