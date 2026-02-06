package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class CrosstownTrafficObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> CrosstownTrafficMarkerObject
            "pebble" -> CrosstownTrafficPebbleObject
            "gem" -> CrosstownTrafficGemObject()
            else -> CrosstownTrafficEmptyObject
        }
    }
}

object CrosstownTrafficEmptyObject : CrosstownTrafficObject()

class CrosstownTrafficHintObject(var state: HintState = HintState.Normal) : CrosstownTrafficObject()

object CrosstownTrafficMarkerObject : CrosstownTrafficObject() {
    override fun objAsString() = "marker"
}

object CrosstownTrafficPebbleObject : CrosstownTrafficObject() {
    override fun objAsString() = "pebble"
}

class CrosstownTrafficGemObject(var state: AllowedObjectState = AllowedObjectState.Normal) : CrosstownTrafficObject() {
    override fun objAsString() = "gem"
}

class CrosstownTrafficGameMove(val p: Position, var obj: CrosstownTrafficObject = CrosstownTrafficEmptyObject)
