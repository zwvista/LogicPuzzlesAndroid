package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ADifferentFarmerObject {
    Empty, ADifferentFarmer, Up, Right, Down, Left
}

class ADifferentFarmerGameMove(val p: Position, var obj: ADifferentFarmerObject = ADifferentFarmerObject.Empty)
