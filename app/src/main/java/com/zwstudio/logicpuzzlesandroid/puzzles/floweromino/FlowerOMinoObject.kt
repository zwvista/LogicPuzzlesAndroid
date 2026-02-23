package com.zwstudio.logicpuzzlesandroid.puzzles.floweromino

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FlowerOMinoObject {
    Empty, Flower, Hedge, Center, Right, Bottom,
    CenterRight, CenterBottom, RightBottom, CenterRightBottom;
    val hasCenter get() = this == Center || this == CenterRight || this == CenterBottom || this == CenterRightBottom
    val hasRight get() = this == Right || this == CenterRight || this == RightBottom || this == CenterRightBottom
    val hasBottom get() = this == Bottom || this == CenterBottom || this == RightBottom || this == CenterRightBottom
}

class FlowerOMinoGameMove(val p: Position, var dir: Int = 0, var obj: GridLineObject = GridLineObject.Empty)
