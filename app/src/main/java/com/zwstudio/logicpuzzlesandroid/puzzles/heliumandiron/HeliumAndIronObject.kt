package com.zwstudio.logicpuzzlesandroid.puzzles.heliumandiron

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class HeliumAndIronObject {
    Empty, Marker, Block, Balloon, Weight
}

class HeliumAndIronGameMove(val p: Position, var obj: HeliumAndIronObject = HeliumAndIronObject.Empty)
