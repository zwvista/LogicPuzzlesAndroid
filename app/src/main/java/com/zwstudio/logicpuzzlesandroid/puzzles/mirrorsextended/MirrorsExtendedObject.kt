package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MirrorsExtendedObject {
    Empty, Forbidden, Marker, Water
}

class MirrorsExtendedGameMove(val p: Position, var obj: MirrorsExtendedObject = MirrorsExtendedObject.Empty)
