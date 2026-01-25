package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweennurikabe

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class InbetweenNurikabeObject {
    Empty, Hint, Marker, Wall
}

class InbetweenNurikabeGameMove(val p: Position, var obj: InbetweenNurikabeObject = InbetweenNurikabeObject.Empty)
