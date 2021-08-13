package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class MinesweeperObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "mine" -> MinesweeperMineObject
            "marker" -> MinesweeperMarkerObject
            else -> MinesweeperEmptyObject
        }
    }
}

object MinesweeperEmptyObject : MinesweeperObject()

object MinesweeperForbiddenObject : MinesweeperObject() {
    override fun objAsString() = "forbidden"
}

class MinesweeperHintObject(var state: HintState = HintState.Normal) : MinesweeperObject() {
    override fun objAsString() = "hint"
}

object MinesweeperMarkerObject : MinesweeperObject() {
    override fun objAsString() = "marker"
}

object MinesweeperMineObject : MinesweeperObject() {
    override fun objAsString() = "mine"
}

class MinesweeperGameMove(val p: Position, var obj: MinesweeperObject = MinesweeperEmptyObject)
