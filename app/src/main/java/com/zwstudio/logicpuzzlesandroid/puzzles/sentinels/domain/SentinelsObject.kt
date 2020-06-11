package com.zwstudio.logicpuzzlesandroid.puzzles.sentinels.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandEmptyObject
import com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain.BootyIslandObject

sealed class SentinelsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> SentinelsMarkerObject()
            "tower" -> SentinelsTowerObject()
            else -> SentinelsMarkerObject()
        }
    }
}

class SentinelsEmptyObject : SentinelsObject()

class SentinelsForbiddenObject : SentinelsObject() {
    override fun objAsString() = "forbidden"
}

class SentinelsHintObject(var state: HintState = HintState.Normal) : SentinelsObject() {
    override fun objAsString() = "hint"
}

class SentinelsMarkerObject : SentinelsObject() {
    override fun objAsString() = "marker"
}

class SentinelsTowerObject(var state: AllowedObjectState = AllowedObjectState.Normal) : SentinelsObject() {
    override fun objAsString() = "tower"
}

class SentinelsGameMove(val p: Position, var obj: SentinelsObject = SentinelsEmptyObject())
