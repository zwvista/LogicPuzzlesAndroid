package com.zwstudio.logicpuzzlesandroid.puzzles.mirrors

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MirrorsGameState(game: MirrorsGame) : CellsGameState<MirrorsGame, MirrorsGameMove, MirrorsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: MirrorsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MirrorsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2) || game[p] == MirrorsGame.PUZ_BLOCK || game[p2] == MirrorsGame.PUZ_BLOCK)
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 10/Mirrors

        Summary
        Zip, swish, zoom! Lasers on mirrors!

        Description
        1. The goal is to draw a single, continuous, non-crossing path that fills
           the entire board.
        2. Some tiles are already given and can contain Mirrors, which force the
           path to make a turn. Other tiles already contain a fixed piece of straight
           path.
        3. Your task is to fill the remaining board tiles with straight or 90 degree
           path lines, in the end connecting a single, continuous line.
        4. Please note you can make 90 degree turn even there are no mirrors.

        Variant
        5. In the Maze variant, the path isn't closed. You have two spots on the
           board which represent the start and end of the path.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a loop that runs through all tiles.
                    pos2dirs[p] = dirs
                else if (!(dirs.isEmpty() && game[p] == MirrorsGame.PUZ_BLOCK)) {
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
            p2 += MirrorsGame.offset[n]
            if (p2 == p) break
        }
    }
}