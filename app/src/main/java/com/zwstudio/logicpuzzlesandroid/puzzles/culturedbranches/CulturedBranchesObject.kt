package com.zwstudio.logicpuzzlesandroid.puzzles.culturedbranches

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class CulturedBranchesObject {
    Empty, Hint, Up, Right, Down, Left, Horizontal, Vertical
}

class CulturedBranchesGameMove(val p: Position, var obj: CulturedBranchesObject = CulturedBranchesObject.Empty)
