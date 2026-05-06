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
        val area2patchArray = game.area2areas.map { areas ->
            val area2patch = mutableMapOf<Int, Int>()
            for (a in areas)
                area2patch[a] = 0
            area2patch
        }
        val row2patch = IntArray(rows)
        val col2patch = IntArray(cols)
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p1 = Position(r, c)
                val a1 = game.pos2area[p1]!!
                for (i in 0..<4) {
                    if (!this[p1][i]) continue
                    val p2 = p1 + JoinMeGame.offset[i]
                    val a2 = game.pos2area[p2]!!
                    if (a1 == a2)
                        isSolved = false
                    else
                        area2patchArray[a1][a2] = area2patchArray[a1][a2]!! + 1
                    if (i == 1 || i == 2) {
                        row2patch[p1.row]++
                        col2patch[p1.col]++
                        row2patch[p2.row]++
                        col2patch[p2.col]++
                    }
                }
            }
        if (!area2patchArray.all {
            it.all { (_, stitch) -> stitch == game.stitches }
        }) isSolved = false
        for (r in 0..<rows) {
            val (n1, n2) = row2patch[r] to game.row2hint[r]
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == JoinMeGame.PUZ_UNKNOWN) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 0..<cols) {
            val (n1, n2) = col2patch[c] to game.col2hint[c]
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == JoinMeGame.PUZ_UNKNOWN) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}