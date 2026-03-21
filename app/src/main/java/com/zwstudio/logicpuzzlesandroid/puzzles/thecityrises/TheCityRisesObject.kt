package com.zwstudio.logicpuzzlesandroid.puzzles.thecityrises

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TheCityRisesObject {
    Empty, Forbidden, Marker, Block
}

class TheCityRisesGameMove(val p: Position, var obj: TheCityRisesObject = TheCityRisesObject.Empty)
