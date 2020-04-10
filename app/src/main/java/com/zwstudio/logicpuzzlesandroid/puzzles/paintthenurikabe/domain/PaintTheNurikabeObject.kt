package com.zwstudio.logicpuzzlesandroid.puzzles.paintthenurikabe.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class PaintTheNurikabeObject {
    Empty, Painted, Forbidden, Marker
}

class PaintTheNurikabeGameMove(val p: Position, var obj: PaintTheNurikabeObject = PaintTheNurikabeObject.Empty)
