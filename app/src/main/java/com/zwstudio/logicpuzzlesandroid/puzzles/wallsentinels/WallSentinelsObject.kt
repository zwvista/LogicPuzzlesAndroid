package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class WallSentinelsObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> WallSentinelsMarkerObject
            "wall" -> WallSentinelsWallObject
            else -> WallSentinelsEmptyObject
        }
    }
}

object WallSentinelsEmptyObject : WallSentinelsObject()

object WallSentinelsWallObject : WallSentinelsObject() {
    override fun objAsString() = "wall"
}

object WallSentinelsMarkerObject : WallSentinelsObject() {
    override fun objAsString() = "marker"
}

class WallSentinelsHintWallObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinelsObject()

class WallSentinelsHintLandObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinelsObject()

class WallSentinelsGameMove(val p: Position, var obj: WallSentinelsObject = WallSentinelsEmptyObject)
