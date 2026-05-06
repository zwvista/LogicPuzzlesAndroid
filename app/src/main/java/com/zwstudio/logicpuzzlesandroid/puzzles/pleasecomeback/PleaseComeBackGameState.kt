package com.zwstudio.logicpuzzlesandroid.puzzles.pleasecomeback

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PleaseComeBackGameState(game: PleaseComeBackGame) : CellsGameState<PleaseComeBackGame, PleaseComeBackGameMove, PleaseComeBackGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PleaseComeBackGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + PleaseComeBackGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2) || game[p] == PleaseComeBackGame.PUZ_BLOCK || game[p2] == PleaseComeBackGame.PUZ_BLOCK)
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 1/Please come back

        Summary
        Just once, then go, ok?

        Description
        1. Draw a single path which passes in each area exactly twice.
        2. Every square in the board must be passed through, except for brown
           areas, which are to be avoided entirely.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a single path
                    pos2dirs[p] = dirs
                else if (dirs.isNotEmpty()) {
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        var lastArea = -1
        val area2count = mutableMapOf<Int, Int>()
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            val area = game.pos2area[p2]!!
            if (area != lastArea) {
                area2count[area] = (area2count[area] ?: 0) + 1
                lastArea = area
            }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += PleaseComeBackGame.offset[n]
            if (p2 == p) {
                if (area == game.pos2area[p]!!)
                    area2count[area] = area2count[area]!! - 1
                break
            }
        }
        // 1. Draw a single path which passes in each area exactly twice.
        // 2. Every square in the board must be passed through, except for brown
        //    areas, which are to be avoided entirely.
        if (!(area2count.size == game.areas.size && area2count.all { it.value == 2 })) isSolved = false
    }
}