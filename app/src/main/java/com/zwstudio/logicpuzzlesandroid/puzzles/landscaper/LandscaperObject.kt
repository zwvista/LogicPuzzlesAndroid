package com.zwstudio.logicpuzzlesandroid.puzzles.landscaper

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class LandscaperObject {
    Empty, Tree, Flower
}

class LandscaperGameMove(val p: Position, var obj: LandscaperObject = LandscaperObject.Empty)
