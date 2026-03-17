package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ChocolateObject {
    Empty, Forbidden, Marker, Chocolate
}

class ChocolateGameMove(val p: Position, var obj: ChocolateObject = ChocolateObject.Empty)
