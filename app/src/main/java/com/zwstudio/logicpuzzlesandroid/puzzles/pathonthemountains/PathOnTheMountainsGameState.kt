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
        if (!isValid(p2) || game[p] == PathOnTheMountainsGame.PUZ_BLOCK || game[p2] == PathOnTheMountainsGame.PUZ_BLOCK)
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
        val pos2Dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a loop that runs through all tiles.
                    pos2Dirs[p] = dirs
                else if (!(dirs.isEmpty() && game[p] == PathOnTheMountainsGame.PUZ_BLOCK)) {
                    // 2. The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2Dirs.keys.first()
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2Dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2Dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += PathOnTheMountainsGame.offset[n]
            if (p2 == p) return
        }
    }
}