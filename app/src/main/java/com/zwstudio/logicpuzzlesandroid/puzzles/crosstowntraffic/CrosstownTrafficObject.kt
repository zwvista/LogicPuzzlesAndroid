package com.zwstudio.logicpuzzlesandroid.puzzles.crosstowntraffic

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CrosstownTrafficObject {
    Empty, Marker, Hint,
    UpRight, DownRight, LeftDown, LeftUp, Horizontal, Vertical, Cross
}

class CrosstownTrafficGameMove(val p: Position, var obj: CrosstownTrafficObject = CrosstownTrafficObject.Empty)
