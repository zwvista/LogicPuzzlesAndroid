package com.zwstudio.logicpuzzlesandroid.puzzles.crossroadblocks

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CrossroadBlocksGameState(game: CrossroadBlocksGame) : CellsGameState<CrossroadBlocksGame, CrossroadBlocksGameMove, CrossroadBlocksGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CrossroadBlocksGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + CrossroadBlocksGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2) || game[p] == CrossroadBlocksGame.PUZ_BLOCK || game[p2] == CrossroadBlocksGame.PUZ_BLOCK)
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 4/Crossroad Blocks

        Summary
        Steer before the roadblock!

        Description
        1. Try to drive around the circuit without hitting the road blocks:
           draw a single closed non-intersecting loop.
        2. The arrows and numbers tell you the total number of cells borders
           the road crosses in that direction.
        3. In the example, looking at the top stretch, the road goes through
           4 cells, hence it crosses 3 cell borders.
        4. Also on the top left, the road goes through 2 cells and so it crosses
           one cell border.
        5. Black cells must be inside the loop. White cells must be outside the loop.
        6. The number tells you the total tiles crossed in that direction.
           So that could be split in two stretches or more.
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
                else if (!(dirs.isEmpty() && game[p] == CrossroadBlocksGame.PUZ_BLOCK)) {
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
            p2 += CrossroadBlocksGame.offset[n]
            if (p2 == p) break
        }
    }
}