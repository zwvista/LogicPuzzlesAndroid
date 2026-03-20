package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class MinesweeperObject {
    Empty, Forbidden, Hint, Marker, Mine
}

class MinesweeperGameMove(val p: Position, var obj: MinesweeperObject = MinesweeperObject.Empty)
