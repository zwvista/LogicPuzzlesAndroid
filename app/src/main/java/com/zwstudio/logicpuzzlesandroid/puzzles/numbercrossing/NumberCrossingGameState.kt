package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrossing

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberCrossingGameState(game: NumberCrossingGame) : CellsGameState<NumberCrossingGame, NumberCrossingGameMove, NumberCrossingGameState>(game) {
    val objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: NumberCrossingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == NumberCrossingGame.PUZ_FORBIDDEN || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NumberCrossingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == NumberCrossingGame.PUZ_FORBIDDEN) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        val o = this[p]
        move.obj =
            if (o == NumberCrossingGame.PUZ_UNKNOWN) if (markerOption == MarkerOptions.MarkerFirst) NumberCrossingGame.PUZ_MARKER else 1
            else if (o == NumberCrossingGame.PUZ_MARKER) if (markerOption == MarkerOptions.MarkerFirst) 1 else NumberCrossingGame.PUZ_UNKNOWN
            else if (o == 9) if (markerOption == MarkerOptions.MarkerLast) NumberCrossingGame.PUZ_MARKER else NumberCrossingGame.PUZ_UNKNOWN
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 1 until rows - 1)
            for (c in 1 until cols - 1) {
                pos2state[Position(r, c)] = HintState.Normal
                if (this[r, c] == NumberCrossingGame.PUZ_FORBIDDEN)
                    this[r, c] = NumberCrossingGame.PUZ_UNKNOWN
            }
        for (r in 1 until rows - 1) {
            val (p1, p2) = Position(r, 0) to Position(r, cols - 1)
            val (h1, h2) = this[p1] to this[p2]
            var (n1, n2) = 0 to 0
            for (c in 1 until cols - 1) {
                val p = Position(r, c)
                val o = this[p]
                if (o < 0) continue
                n1 += 1; n2 += o
                // 2. Numbers cannot touch each other, not even diagonally.
                for (os in NumberCrossingGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    val o2 = this[p2]
                    if (o2 > 0) {
                        pos2state[p] = HintState.Error
                        isSolved = false
                    } else if (allowedObjectsOnly && o2 == NumberCrossingGame.PUZ_UNKNOWN)
                        this[p2] = NumberCrossingGame.PUZ_FORBIDDEN
                }
            }
            // 3. On the top and left of the grid, you're given how many numbers are in that
            //    column or row.
            // 4. On the bottom and right of the grid, you're given the sum of the numbers
            //    on that column or row.
            val s1 = if (h1 == NumberCrossingGame.PUZ_UNKNOWN || n1 == h1) HintState.Complete else if (n1 < h1) HintState.Normal else HintState.Error
            val s2 = if (h2 == NumberCrossingGame.PUZ_UNKNOWN || n2 == h2) HintState.Complete else if (n2 < h2) HintState.Normal else HintState.Error
            pos2state[p1] = s1; pos2state[p2] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && (
                        h1 != NumberCrossingGame.PUZ_UNKNOWN && s1 != HintState.Normal ||
                        h2 != NumberCrossingGame.PUZ_UNKNOWN && s2 != HintState.Normal))
                for (c in 1 until cols - 1)
                    if (this[r, c] == NumberCrossingGame.PUZ_UNKNOWN)
                        this[r, c] = NumberCrossingGame.PUZ_FORBIDDEN
        }
        for (c in 1 until cols - 1) {
            val (p1, p2) = Position(0, c) to Position(rows - 1, c)
            val (h1, h2) = this[p1] to this[p2]
            var (n1, n2) = 0 to 0
            for (r in 1 until rows - 1) {
                val p = Position(r, c)
                val o = this[p]
                if (o < 0) continue
                n1 += 1; n2 += o
                // 2. Numbers cannot touch each other, not even diagonally.
                for (os in NumberCrossingGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    val o2 = this[p2]
                    if (o2 > 0) {
                        pos2state[p] = HintState.Error
                        isSolved = false
                    } else if (allowedObjectsOnly && o2 == NumberCrossingGame.PUZ_UNKNOWN)
                        this[p2] = NumberCrossingGame.PUZ_FORBIDDEN
                }
            }
            // 3. On the top and left of the grid, you're given how many numbers are in that
            //    column or row.
            // 4. On the bottom and right of the grid, you're given the sum of the numbers
            //    on that column or row.
            val s1 = if (h1 == NumberCrossingGame.PUZ_UNKNOWN || n1 == h1) HintState.Complete else if (n1 < h1) HintState.Normal else HintState.Error
            val s2 = if (h2 == NumberCrossingGame.PUZ_UNKNOWN || n2 == h2) HintState.Complete else if (n2 < h2) HintState.Normal else HintState.Error
            pos2state[p1] = s1; pos2state[p2] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            if (allowedObjectsOnly && (
                        h1 != NumberCrossingGame.PUZ_UNKNOWN && s1 != HintState.Normal ||
                        h2 != NumberCrossingGame.PUZ_UNKNOWN && s2 != HintState.Normal))
                for (r in 1 until rows - 1)
                    if (this[r, c] == NumberCrossingGame.PUZ_UNKNOWN)
                        this[r, c] = NumberCrossingGame.PUZ_FORBIDDEN
        }
    }
}