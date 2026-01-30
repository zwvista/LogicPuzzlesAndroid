package com.zwstudio.logicpuzzlesandroid.puzzles.branches

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class BranchesObject {
    Empty, Hint, Up, Right, Down, Left, Horizontal, Vertical
}

class BranchesGameMove(val p: Position, var obj: BranchesObject = BranchesObject.Empty)
