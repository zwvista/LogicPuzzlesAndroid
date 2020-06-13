package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class AbstractPaintingObject {
    Empty, Forbidden, Marker, Painting
}

class AbstractPaintingGameMove(val p: Position, var obj: AbstractPaintingObject = AbstractPaintingObject.Empty)

