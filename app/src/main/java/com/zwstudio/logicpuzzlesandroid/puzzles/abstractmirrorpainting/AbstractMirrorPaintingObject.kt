package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class AbstractMirrorPaintingObject {
    Empty, Painted, Forbidden, Marker
}

class AbstractMirrorPaintingGameMove(val p: Position, var obj: AbstractMirrorPaintingObject = AbstractMirrorPaintingObject.Empty)
