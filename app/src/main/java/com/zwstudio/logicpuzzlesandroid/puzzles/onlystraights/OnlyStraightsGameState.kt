package com.zwstudio.logicpuzzlesandroid.puzzles.onlystraights

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class OnlyStraightsGameState(game: OnlyStraightsGame) : CellsGameState<OnlyStraightsGame, OnlyStraightsGameMove, OnlyStraightsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: OnlyStraightsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + OnlyStraightsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 4/Only Straights

        Summary
        We loooove long straights

        Description
        1. Draw a non-intersecting loop that visits all towns.
        2. This time, you must go straight while passing a town.
        3. Branches of a road coming off a town must be of equal length.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2) {
                    pos2dirs[p] = dirs
                    if (game[p] != ' ')
                        // 2. This time, you must go straight while passing a town.
                        if (dirs[1] - dirs[0] != 2) {
                            isSolved = false; return
                        }
                } else if (dirs.isNotEmpty()) {
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += OnlyStraightsGame.offset[n]
            if (p2 == p) break
        }
        // 3. Branches of a road coming off a town must be of equal length.
        if (!pos2dirs.all { (p, dirs) ->
            fun f(d: Int): Int {
                val os = OnlyStraightsGame.offset[d]
                var p2 = p + os
                var n = 0
                while (pos2dirs[p2]!!.contains(d)) {
                    n++
                    p2 += os
                }
                return n
            }
            game[p] == ' ' || f(dirs[0]) == f(dirs[1])
        }) isSolved = false
    }
}