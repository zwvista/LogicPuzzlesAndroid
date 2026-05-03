package com.zwstudio.logicpuzzlesandroid.puzzles.farmer

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class FarmerObject {
    Empty, Fv1, Fv2, Fv3
}

class FarmerGameMove(val p: Position, var obj: FarmerObject = FarmerObject.Empty)
