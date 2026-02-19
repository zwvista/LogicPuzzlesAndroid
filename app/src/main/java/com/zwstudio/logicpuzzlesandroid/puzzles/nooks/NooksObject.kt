package com.zwstudio.logicpuzzlesandroid.puzzles.nooks

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class NooksObject {
    Empty, Hint, Marker,
    Hedge;

    val isHedge get() = this == Hedge
    val isEmpty get() = this != Hedge
}

class NooksGameMove(val p: Position, var obj: NooksObject = NooksObject.Empty)
