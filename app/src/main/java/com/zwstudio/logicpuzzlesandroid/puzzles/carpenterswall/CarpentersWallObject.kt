package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CarpentersWallObject(val isHint: Boolean = false) {
    Empty, Marker, Corner, Wall, Up, Down, Left, Right
}

class CarpentersWallGameMove(val p: Position, var obj: CarpentersWallObject = CarpentersWallObject.Empty)
