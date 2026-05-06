package com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RunInALoopGameState(game: RunInALoopGame) : CellsGameState<RunInALoopGame, RunInALoopGameMove, RunInALoopGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: RunInALoopGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + RunInALoopGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2) || game[p] == RunInALoopGame.PUZ_BLOCK || game[p2] == RunInALoopGame.PUZ_BLOCK)
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Run in a Loop

        Summary
        Loop a loop

        Description
        1. Draw a loop that runs through all tiles.
        2. The loop cannot cross itself.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a loop that runs through all tiles.
                    pos2dirs[p] = dirs
                else if (!(dirs.isEmpty() && game[p] == RunInALoopGame.PUZ_BLOCK)) {
                    // 2. The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.first()
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += RunInALoopGame.offset[n]
            if (p2 == p) break
        }
    }
}