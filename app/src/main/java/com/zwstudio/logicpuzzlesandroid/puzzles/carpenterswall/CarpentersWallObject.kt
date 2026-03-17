package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CarpentersWallObject {
    Empty, Marker, Corner, Wall, Up, Down, Left, Right;
    val isHint get() = this == Corner || this == Up || this == Down || this == Left || this == Right
}

class CarpentersWallGameMove(val p: Position, var obj: CarpentersWallObject = CarpentersWallObject.Empty)
