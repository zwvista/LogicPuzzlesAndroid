package com.zwstudio.logicpuzzlesandroid.puzzles.themagicnumber

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class TheMagicNumberObject {
    Empty, Forbidden, Marker, Flower, Block
}

class TheMagicNumberGameMove(val p: Position, var obj: TheMagicNumberObject = TheMagicNumberObject.Empty)
