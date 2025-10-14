package com.zwstudio.logicpuzzlesandroid.puzzles.arrows

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ArrowsGameState(game: ArrowsGame) : CellsGameState<ArrowsGame, ArrowsGameMove, ArrowsGameState>(game) {
    val objArray = game.objArray.copyOf()
    var hint2state = mutableMapOf<Position, HintState>()
    var arrow2state = mutableMapOf<Position, AllowedObjectState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    override fun setObject(move: ArrowsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ArrowsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = get(p)
        move.obj = (o + 1) % 9
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 6/Arrows

        Summary
        Just Arrows?

        Description
        1. The goal is to detect the arrows directions that reside outside the board.
        2. Each Arrow points to at least one number inside the board.
        3. The numbers tell you how many arrows point at them.
        4. There is one arrow for each tile outside the board.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = this[p]
                if (game.isCorner(p))
                    ;
                else if (game.isBorder(p)) {
                    if (n == ArrowsGame.PUZ_UNKNOWN)
                        arrow2state[p] = AllowedObjectState.Normal
                    else {
                        val os = ArrowsGame.offset[n]
                        var p2 = p + os
                        var n2 = 0
                        while (isValid(p2)) {
                            if (!(game.isCorner(p2) || game.isBorder(p2)))
                                n2++
                            p2 += os
                        }
                        // 2. Each Arrow points to at least one number inside the board.
                        val s = if (n2 > 0) AllowedObjectState.Normal else AllowedObjectState.Error
                        arrow2state[p] = s
                        if (s == AllowedObjectState.Error) isSolved = false
                    }
                } else {
                    var n2 = 0
                    for (i in 0 until 8) {
                        val os = ArrowsGame.offset[i]
                        var p2 = p + os
                        while (isValid(p2)) {
                            if (game.isBorder(p2) && this[p2] == (i + 4) % 8)
                                n2++
                            p2 += os
                        }
                        // 3. The numbers tell you how many arrows point at them.
                        val s = if (n2 < n) HintState.Normal else if (n2 == n) HintState.Complete else HintState.Error
                        hint2state[p] = s
                        if (s == HintState.Error) isSolved = false
                    }
                }
            }
    }
}
