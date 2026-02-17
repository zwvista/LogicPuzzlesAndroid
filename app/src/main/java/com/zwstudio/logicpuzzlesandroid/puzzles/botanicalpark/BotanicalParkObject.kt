package com.zwstudio.logicpuzzlesandroid.puzzles.botanicalpark

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BotanicalParkObject {
    Empty, Forbidden, Marker,
    Plant, Arrow
}

class BotanicalParkGameMove(val p: Position, var obj: BotanicalParkObject = BotanicalParkObject.Empty)
