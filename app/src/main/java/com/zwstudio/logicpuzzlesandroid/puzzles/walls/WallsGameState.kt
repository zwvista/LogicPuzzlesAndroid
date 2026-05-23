package com.zwstudio.logicpuzzlesandroid.puzzles.walls

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class WallsGameState(game: WallsGame) : CellsGameState<WallsGame, WallsGameMove, WallsGameState>(game) {
    val objArray = Array(rows * cols) { WallsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: WallsObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: WallsObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = WallsObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: WallsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: WallsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            WallsObject.Empty -> WallsObject.Horz
            WallsObject.Horz -> WallsObject.Vert
            WallsObject.Vert -> WallsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Walls

        Summary
        Find the maze of Bricks

        Description
        1. In Walls you must fill the board with straight horizontal and
           vertical lines (walls) that stem from each number.
        2. The number itself tells you the total length of Wall segments
           connected to it.
        3. Wall pieces have two ways to be put, horizontally or vertically.
        4. Not every wall piece must be connected with a number, but the
           board must be filled with wall pieces.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o == WallsObject.Empty) // 1. In Walls you must fill the board with straight horizontal and
                // vertical lines (walls) that stem from each number.
                // 4. Not every wall piece must be connected with a number, but the
                // board must be filled with wall pieces.
                    isSolved = false
                else if (o == WallsObject.Hint) {
                    val n2 = game.pos2hint[p]!!
                    var n1 = 0
                    for (i in 0..<4) {
                        val os = WallsGame.offset[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (i % 2 == 0) // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                if (this[p2] == WallsObject.Vert)
                                    n1++
                                else
                                    break
                            else  // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                if (this[p2] == WallsObject.Horz)
                                    n1++
                                else
                                    break
                            p2 += os
                        }
                    }
                    // 2. The number itself tells you the total length of Wall segments
                    // connected to it.
                    val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                    if (s != HintState.Complete) isSolved = false
                    pos2state[p] = s
                }
            }
    }
}