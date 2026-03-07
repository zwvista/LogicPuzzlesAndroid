package com.zwstudio.logicpuzzlesandroid.puzzles.landscapes

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LandscapesObject {
    Empty, Tree, Sand, Water, Rock
}

class LandscapesGameMove(val p: Position, var obj: LandscapesObject = LandscapesObject.Empty)
