package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FarmerObject {
    Empty, Farmer, Up, Right, Down, Left
}

class FarmerGameMove(val p: Position, var obj: FarmerObject = FarmerObject.Empty)
