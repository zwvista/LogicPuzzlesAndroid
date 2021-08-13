package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class SentinelsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> SentinelsMarkerObject
            "tower" -> SentinelsTowerObject()
            else -> SentinelsMarkerObject
        }
    }
}

object SentinelsEmptyObject : SentinelsObject()

object SentinelsForbiddenObject : SentinelsObject() {
    override fun objAsString() = "forbidden"
}

class SentinelsHintObject(var state: HintState = HintState.Normal) : SentinelsObject() {
    override fun objAsString() = "hint"
}

object SentinelsMarkerObject : SentinelsObject() {
    override fun objAsString() = "marker"
}

class SentinelsTowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : SentinelsObject() {
    override fun objAsString() = "tower"
}

class SentinelsGameMove(val p: Position, var obj: SentinelsObject = SentinelsEmptyObject)
