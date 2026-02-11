package com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class YalooniqGameState(game: YalooniqGame) : CellsGameState<YalooniqGame, YalooniqGameMove, YalooniqGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: YalooniqGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + YalooniqGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2) || game[p] == YalooniqGame.PUZ_BLOCK || game[p2] == YalooniqGame.PUZ_BLOCK)
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 6/Yalooniq

        Summary
        Loops, Arrows and Squares

        Description
        1. The goal is to draw a single Loop on the board, similarly to LineSweeper.
        2. The Loop must go through ALL the available tiles on the board.
        3. The available tiles on which the Loop must go are the ones without
           Arrows and also not containing Squares.
        4. It is up to you to find the Squares, which are pointed at by the Arrows!
        5. The numbers beside the Arrows tell you how many Squares are present
           in that direction, from that point.
        6. The Squares can't touch horizontally or vertically.
        7. Lastly, please keep in mind that if there aren't Arrows pointing to
           a tile, that tile can contain a Square too!
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
                else if (!(dirs.isEmpty() && game[p] == YalooniqGame.PUZ_BLOCK)) {
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
            p2 += YalooniqGame.offset[n]
            if (p2 == p) break
        }
    }
}