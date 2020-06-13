package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

sealed class WallSentinelsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> WallSentinelsMarkerObject()
            "wall" -> WallSentinelsWallObject()
            else -> WallSentinelsEmptyObject()
        }
    }
}

class WallSentinelsEmptyObject : WallSentinelsObject()

class WallSentinelsWallObject : WallSentinelsObject() {
    override fun objAsString() = "wall"
}

class WallSentinelsMarkerObject : WallSentinelsObject() {
    override fun objAsString() = "marker"
}

class WallSentinelsHintWallObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinelsObject()

class WallSentinelsHintLandObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinelsObject()

class WallSentinelsGameMove(val p: Position, var obj: WallSentinelsObject = WallSentinelsEmptyObject())
