package com.zwstudio.logicpuzzlesandroid.puzzles.adifferentfarmer

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class ADifferentFarmerObject {
    Empty, Fv1, Fv2, Fv3
}

class ADifferentFarmerGameMove(val p: Position, var obj: ADifferentFarmerObject = ADifferentFarmerObject.Empty)
