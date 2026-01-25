package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class NurikabeObject {
    Empty, Hint, Marker, Wall
}

class NurikabeGameMove(val p: Position, var obj: NurikabeObject = NurikabeObject.Empty)
