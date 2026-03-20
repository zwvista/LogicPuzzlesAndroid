package com.zwstudio.logicpuzzlesandroid.puzzles.parklakes

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ParkLakesObject {
    Empty, Hint, Marker, Lake
}

class ParkLakesGameMove(val p: Position, var obj: ParkLakesObject = ParkLakesObject.Empty)
