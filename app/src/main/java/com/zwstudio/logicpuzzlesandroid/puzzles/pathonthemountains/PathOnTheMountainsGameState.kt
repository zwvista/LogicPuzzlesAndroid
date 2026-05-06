package com.zwstudio.logicpuzzlesandroid.puzzles.pathonthemountains

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PathOnTheMountainsGameState(game: PathOnTheMountainsGame) : CellsGameState<PathOnTheMountainsGame, PathOnTheMountainsGameMove, PathOnTheMountainsGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PathOnTheMountainsGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + PathOnTheMountainsGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 5/Path on the Mountains

        Summary
        Turn on the peak, turn on the plain

        Description
        1. Fill the board with a loop that passes through all tiles.
        2. The path should make 90 degrees turns on the spots.
        3. Between spots, the path makes one more 90 degrees turn.
        4. So the path alternates turning on spots and outside them.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
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
            p2 += PathOnTheMountainsGame.offset[n]
            if (game[p2] != ' ') {
                // 3. Between spots, the path makes one more 90 degrees turn.
                val turns = (0..<ns.size - 1).count { ns[it] != ns[it + 1] }
                if (turns != 1) { isSolved = false; return }
                ns.clear()
            }
            if (p2 == p) break
        }
    }
}