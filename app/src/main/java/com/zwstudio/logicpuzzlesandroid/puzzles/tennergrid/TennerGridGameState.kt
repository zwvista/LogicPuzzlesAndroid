package com.zwstudio.logicpuzzlesandroid.puzzles.tennergrid

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TennerGridGameState(game: TennerGridGame) : CellsGameState<TennerGridGame, TennerGridGameMove, TennerGridGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: TennerGridGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] >= 0 || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: TennerGridGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] >= 0) return false
        val o = this[p]
        move.obj = if (o == 9) -1 else o + 1
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 7/TennerGrid

        Summary
        Counting up to 10

        Description
        1. You goal is to enter every digit, from 0 to 9, in each row of the Grid.
        2. The number on the bottom row gives you the sum for that column.
        3. Digit can repeat on the same column, however digits in contiguous tiles
           must be different, even diagonally. Obviously digits can't repeat on
           the same row.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows - 1) {
            val cs = (0 until cols).groupBy { this[r, it] }
                .filter { (k, v) -> k != -1 && v.size > 1 }
                .flatMap { it.value }
            // 3. Obviously digits can't repeat on the same row.
            if (cs.isNotEmpty()) isSolved = false
            for (c in 0 until cols) {
                val p = Position(r, c)
                pos2state[p] = if (cs.contains(c)) HintState.Error else HintState.Normal
            }
        }
        for (c in 0 until cols) {
            val h = this[rows - 1, c]
            var n = 0
            var isDirty = false
            var allFixed = true
            for (r in 0 until rows - 1) {
                val p = Position(r, c)
                val o1 = game[p]
                val o2 = this[p]
                if (o1 == -1) {
                    allFixed = false
                    if (o2 == -1)
                        isSolved = false
                    else
                        isDirty = true
                }
                n += if (o2 == -1) 0 else o2
                // 3. Digit can repeat on the same column, however digits in contiguous tiles
                // must be different, even diagonally.
                if (r < rows - 2) {
                    val rng = TennerGridGame.offset.map { p + it }.filter { isValid(it) && o2 == this[it] }
                    if (rng.isNotEmpty()) {
                        isSolved = false
                        pos2state[p] = HintState.Error
                        for (p2 in rng)
                            pos2state[p2] = HintState.Error
                    }
                }
            }
            // 2. The number on the bottom row gives you the sum for that column.
            val s: HintState = if (!isDirty && !allFixed) HintState.Normal else if (n == h) HintState.Complete else HintState.Error
            pos2state[Position(rows - 1, c)] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}