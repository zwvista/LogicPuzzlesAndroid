package com.zwstudio.logicpuzzlesandroid.puzzles.disconnectfour.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class DisconnectFourObject {
    Empty, Yellow, Red
}

class DisconnectFourGameMove(val p: Position, var obj: DisconnectFourObject = DisconnectFourObject.Empty)
