package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthehills

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PathOnTheHillsGameState(game: PathOnTheHillsGame) : CellsGameState<PathOnTheHillsGame, PathOnTheHillsGameMove, PathOnTheHillsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PathOnTheHillsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + PathOnTheHillsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Path on the Hills

        Summary
        Up for a little exercise?

        Description
        1. The board represents a map of the Countryside, divided in Fields.
        2. The object is to have a walk around the Countryside, passing through
           each Field just once.
        3. the number on a Field tells you how many tiles you should go through it.
        4. A Field with no number can be passed through in any number of tiles,
           at least one.
        5. If you avoid two adjacent tiles in your path, they should be in the
           same Fields.
        6. Or in other words, two adjacent empty tiles cannot be in two different
           Fields.
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
        val p = pos2dirs.keys.firstOrNull { game[it] != ' ' }
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        val ns = mutableListOf<Int>()
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            ns.add(n)
            p2 += PathOnTheHillsGame.offset[n]
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