package com.zwstudio.logicpuzzlesandroid.puzzles.straightandturn

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class StraightAndTurnGameState(game: StraightAndTurnGame) : CellsGameState<StraightAndTurnGame, StraightAndTurnGameMove, StraightAndTurnGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: StraightAndTurnGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + StraightAndTurnGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Straight and Turn

        Summary
        Straight and Turn

        Description
        1. Draw a path that crosses all gems and follows this rule:
        2. Crossing two adjacent gems:
        3. The line cannot cross two adjacent gems if they are of different color.
        4. The line is free to either go straight or turn when crossing two
           adjacent gems of the same color.
        5. Crossing a gem that is not adjacent to the last crossed:
        6. The line should go straight in the space between two gems of the same
           colour.
        7. The line should make a single 90 degree turn in the space between
           two gems of different colour.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2Dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a path that crosses all gems
                    pos2Dirs[p] = dirs
                else if (!(dirs.isEmpty() && game[p] == ' ')) {
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
        var ch = game[p]
        while (true) {
            val dirs = pos2Dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2Dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            ns.add(n)
            p2 += StraightAndTurnGame.offset[n]
            val ch2 = game[p2]
            if (ch2 != ' ') {
                // 2. Crossing two adjacent gems:
                // 3. The line cannot cross two adjacent gems if they are of different color.
                // 4. The line is free to either go straight or turn when crossing two
                //    adjacent gems of the same color.
                // 5. Crossing a gem that is not adjacent to the last crossed:
                // 6. The line should go straight in the space between two gems of the same
                //    colour.
                // 7. The line should make a single 90 degree turn in the space between
                //    two gems of different colour.
                val turns = (0 until ns.size - 1).count { ns[it] != ns[it + 1] }
                if (!(ch == ch2 && turns == 0 || ch != ch2 && turns == 1)) {
                    isSolved = false; return
                }
                ch = ch2
                ns.clear()
            }
            if (p2 == p) return
        }
    }
}