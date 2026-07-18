package com.zwstudio.logicpuzzlesandroid.puzzles.mirrorsextended

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MirrorsExtendedObject {
    Empty, Forbidden, Marker, Backward, Forward
}

data class MirrorsExtendedLaserDot(val p: Position, val dir: Int)

class MirrorsExtendedLaser(val number: Int) {
    val dots = mutableListOf<MirrorsExtendedLaserDot>()
}

class MirrorsExtendedGameMove(val p: Position, var obj: MirrorsExtendedObject = MirrorsExtendedObject.Empty)
