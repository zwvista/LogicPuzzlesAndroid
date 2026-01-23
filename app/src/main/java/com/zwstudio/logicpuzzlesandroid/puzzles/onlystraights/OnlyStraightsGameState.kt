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
        val pos2Dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2) {
                    pos2Dirs[p] = dirs
                    if (game[p] != ' ')
                        // 2. The path should make 90 degrees turns on the spots.
                        if (dirs[1] - dirs[0] == 2) {
                            isSolved = false; return
                        }
                } else {
                    // 1. Fill the board with a loop that passes through all tiles.
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2Dirs.keys.firstOrNull { game[it] != ' ' }
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        val ns = mutableListOf<Int>()
        while (true) {
            val dirs = pos2Dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2Dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            ns.add(n)
            p2 += OnlyStraightsGame.offset[n]
            if (game[p2] != ' ') {
                // 3. Between spots, the path makes one more 90 degrees turn.
                val turns = (0 until ns.size - 1).count { ns[it] != ns[it + 1] }
                if (turns != 1) { isSolved = false; return }
                ns.clear()
            }
            if (p2 == p) return
        }
    }
}