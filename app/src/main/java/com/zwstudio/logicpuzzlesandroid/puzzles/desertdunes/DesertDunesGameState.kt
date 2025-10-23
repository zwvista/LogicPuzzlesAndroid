package com.zwstudio.logicpuzzlesandroid.puzzles.desertdunes

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DesertDunesGameState(game: DesertDunesGame) : CellsGameState<DesertDunesGame, DesertDunesGameMove, DesertDunesGameState>(game) {
    var objArray = Array<DesertDunesObject>(rows * cols) { DesertDunesEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: DesertDunesObject) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: DesertDunesObject) {this[p.row, p.col] = obj}

    init {
        for ((p, n) in game.pos2hint)
            this[p] = DesertDunesHintObject()
        updateIsSolved()
    }

    override fun setObject(move: DesertDunesGameMove): GameOperationType {
        if (!isValid(move.p) || this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DesertDunesGameMove): GameOperationType {
        val o = this[move.p]
        move.obj = if (o is DesertDunesEmptyObject) DesertDunesHorzObject else if (o is DesertDunesHorzObject) DesertDunesVertObject else if (o is DesertDunesVertObject) DesertDunesEmptyObject else o
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/DesertDunes

        Summary
        Find the maze of Bricks

        Description
        1. In DesertDunes you must fill the board with straight horizontal and
           vertical lines (walls) that stem from each number.
        2. The number itself tells you the total length of Wall segments
           connected to it.
        3. Wall pieces have two ways to be put, horizontally or vertically.
        4. Not every wall piece must be connected with a number, but the
           board must be filled with wall pieces.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                if (o is DesertDunesEmptyObject) // 1. In DesertDunes you must fill the board with straight horizontal and
                // vertical lines (walls) that stem from each number.
                // 4. Not every wall piece must be connected with a number, but the
                // board must be filled with wall pieces.
                    isSolved = false
                else if (o is DesertDunesHintObject) {
                    val n2 = game.pos2hint[p]!!
                    var n1 = 0
                    for (i in 0 until 4) {
                        val os = DesertDunesGame.offset[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (i % 2 == 0) // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                if (this[p2] is DesertDunesVertObject)
                                    n1++
                                else
                                    break
                            else  // 3. Wall pieces have two ways to be put, horizontally or vertically.
                                if (this[p2] is DesertDunesHorzObject)
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
                    o.state = s
                }
            }
    }
}