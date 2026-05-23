package com.zwstudio.logicpuzzlesandroid.puzzles.snakeislands

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SnakeIslandsObject(var lightness: Int = 0) {
    Empty, Marker, Wall, Hint
}

class SnakeIslandsGameMove(val p: Position, var obj: SnakeIslandsObject = SnakeIslandsObject.Empty)
