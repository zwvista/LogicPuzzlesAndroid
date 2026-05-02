package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PlanetsObject {
    Empty, Forbidden, Marker, Flower, Block
}

class PlanetsGameMove(val p: Position, var obj: PlanetsObject = PlanetsObject.Empty)
