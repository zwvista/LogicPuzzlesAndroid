package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class WallSentinels2Object {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) =
            when (str) {
                "marker" -> WallSentinels2MarkerObject()
                "wall" -> WallSentinels2WallObject()
                else -> WallSentinels2EmptyObject()
            }
    }
}

class WallSentinels2EmptyObject : WallSentinels2Object()

class WallSentinels2WallObject : WallSentinels2Object() {
    override fun objAsString() = "wall"
}

class WallSentinels2MarkerObject : WallSentinels2Object() {
    override fun objAsString() = "marker"
}

class WallSentinels2HintWallObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinels2Object()

class WallSentinels2HintLandObject(val tiles: Int, var state: HintState = HintState.Normal) : WallSentinels2Object()

class WallSentinels2GameMove(val p: Position, var obj: WallSentinels2Object = WallSentinels2EmptyObject())
