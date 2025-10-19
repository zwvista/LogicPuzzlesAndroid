package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrossing

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberCrossingGameState(game: NumberCrossingGame) : CellsGameState<NumberCrossingGame, NumberCrossingGameMove, NumberCrossingGameState>(game) {
    val objArray = game.objArray.copyOf()
    var row2state = Array(rows * 2) { HintState.Normal }
    var col2state = Array(cols * 2) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}
    fun getState(row: Int, col: Int) = when {
        row == 0 && col >= 1 && col < cols - 1 -> col2state[col * 2]
        row == rows - 1 && col >= 1 && col < cols - 1 -> col2state[col * 2 + 1]
        col == 0 && row >= 1 && row < rows - 1 -> row2state[row * 2]
        col == cols - 1 && row >= 1 && row < rows - 1 -> row2state[row * 2 + 1]
        else -> HintState.Normal
    }

    init {
        updateIsSolved()
    }

    override fun setObject(move: NumberCrossingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NumberCrossingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = this[p]
        move.obj =
            if (o == NumberCrossingGame.PUZ_UNKNOWN) 1
            else if (o == game.intMax()) NumberCrossingGame.PUZ_UNKNOWN
            else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games/Puzzle Set 17/Number Crossing

        Summary
        Digital Crosswords

        Description
        1. Find the numbers in the board.
        2. Numbers cannot touch each other, not even diagonally.
        3. On the top and left of the grid, you're given how many numbers are in that
           column or row.
        4. On the bottom and right of the grid, you're given the sum of the numbers
           on that column or row.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 1 until rows - 1) {
            val (h1, h2) = this[r, 0] to this[r, cols - 1]
            var (n1, n2) = 0 to 0
            for (c in 1 until cols - 1) {
                val o = this[r, c]
                if (o == NumberCrossingGame.PUZ_UNKNOWN) continue
                n1 += 1; n2 += o
            }
            // 3. On the top and left of the grid, you're given how many numbers are in that
            //    column or row.
            // 4. On the bottom and right of the grid, you're given the sum of the numbers
            //    on that column or row.
            val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            row2state[r * 2] = s1; row2state[r * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
        }
        for (c in 1 until cols - 1) {
            val (h1, h2) = this[0, c] to this[rows - 1, c]
            var (n1, n2) = 0 to 0
            for (r in 1 until rows - 1) {
                val o = this[r, c]
                if (o == NumberCrossingGame.PUZ_UNKNOWN) continue
                n1 += 1; n2 += o
            }
            // 3. On the top and left of the grid, you're given how many numbers are in that
            //    column or row.
            // 4. On the bottom and right of the grid, you're given the sum of the numbers
            //    on that column or row.
            val s1 = if (n1 < h1) HintState.Normal else if (n1 == h1) HintState.Complete else HintState.Error
            val s2 = if (n2 < h2) HintState.Normal else if (n2 == h2) HintState.Complete else HintState.Error
            col2state[c * 2] = s1; col2state[c * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
        }
    }
}