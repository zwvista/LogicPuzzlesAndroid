package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class BotanicalParkObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> BotanicalParkMarkerObject
            "tree" -> BotanicalParkTreeObject()
            else -> BotanicalParkEmptyObject
        }
    }
}

object BotanicalParkEmptyObject : BotanicalParkObject()

object BotanicalParkForbiddenObject : BotanicalParkObject() {
    override fun objAsString() = "forbidden"
}

object BotanicalParkMarkerObject : BotanicalParkObject() {
    override fun objAsString() = "marker"
}

class BotanicalParkTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : BotanicalParkObject() {
    override fun objAsString() = "tree"
}

class BotanicalParkArrowObject(var state: AllowedObjectState = AllowedObjectState.Normal) : BotanicalParkObject() {
    override fun objAsString() = "arrow"
}

class BotanicalParkGameMove(val p: Position, var obj: BotanicalParkObject = BotanicalParkEmptyObject)
