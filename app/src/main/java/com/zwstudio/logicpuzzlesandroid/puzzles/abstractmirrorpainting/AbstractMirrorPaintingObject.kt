package com.zwstudio.logicpuzzlesandroid.puzzles.abstractmirrorpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class AbstractMirrorPaintingObject {
    Empty, Painted, Forbidden, Marker
}

data class AbstractMirrorPaintingMirror(val p1: Position, val p2: Position, val areaId1: Int, val areaId2: Int)

class AbstractMirrorPaintingGameMove(val p: Position, var obj: AbstractMirrorPaintingObject = AbstractMirrorPaintingObject.Empty)
