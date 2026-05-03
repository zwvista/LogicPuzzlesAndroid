package com.zwstudio.logicpuzzlesandroid.puzzles.planets

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PlanetsObject {
    Empty,
    None, North, NorthEast, NorthWest, East, West, South, SouthEast, SouthWest,
    Forbidden, Marker,
    Sun, Nebula
}

class PlanetsGameMove(val p: Position, var obj: PlanetsObject = PlanetsObject.Empty)
