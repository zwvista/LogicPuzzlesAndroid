package com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class WallSentinelsObject {
    Empty, Marker, HintWall, HintLand, Wall
}

class WallSentinelsGameMove(val p: Position, var obj: WallSentinelsObject = WallSentinelsObject.Empty)
