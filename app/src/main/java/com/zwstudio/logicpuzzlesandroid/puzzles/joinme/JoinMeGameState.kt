package com.zwstudio.logicpuzzlesandroid.puzzles.joinme

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class JoinMeGameState(game: JoinMeGame) : CellsGameState<JoinMeGame, JoinMeGameMove, JoinMeGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Array<Boolean>) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: JoinMeGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + JoinMeGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Join Me!

        Summary
        Communicating Vessels

        Description
        1. Connect the different patches with one stitch (more in later levels).
        2. The numbers on the outside tell you how many stitches you can see from
           there in the row/column.
        3. A cell can contain only one stitch.
        4. Later levels will show you in the top right how many stitches you have
           to put between patches.
    */
    private fun updateIsSolved() {
        isSolved = true
    }
}