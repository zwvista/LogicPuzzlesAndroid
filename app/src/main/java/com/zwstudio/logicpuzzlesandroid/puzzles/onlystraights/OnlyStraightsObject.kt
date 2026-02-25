package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class OnlyStraightsTown {
    Empty, Center, Right, Bottom,
    CenterRight, CenterBottom, RightBottom, CenterRightBottom;
    val hasCenter get() = this == Center || this == CenterRight || this == CenterBottom || this == CenterRightBottom
    val hasRight get() = this == Right || this == CenterRight || this == RightBottom || this == CenterRightBottom
    val hasBottom get() = this == Bottom || this == CenterBottom || this == RightBottom || this == CenterRightBottom
}

class OnlyStraightsGameMove(val p: Position, var dir: Int = 0)
