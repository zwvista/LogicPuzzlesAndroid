package com.zwstudio.logicpuzzlesandroid.puzzles.thermometers

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ThermometersObject {
    Empty, Forbidden, Marker, Filled
}

class ThermometersGameMove(val p: Position, var obj: ThermometersObject = ThermometersObject.Empty)
