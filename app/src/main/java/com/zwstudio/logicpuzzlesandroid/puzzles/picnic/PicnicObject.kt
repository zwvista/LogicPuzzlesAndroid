package com.zwstudio.logicpuzzlesandroid.puzzles.picnic

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PicnicObject {
    Empty, Hint, Marker,
    Hedge;

    val isHedge get() = this == Hedge
    val isEmpty get() = this != Hedge
}

class PicnicGameMove(val p: Position, var obj: PicnicObject = PicnicObject.Empty)
