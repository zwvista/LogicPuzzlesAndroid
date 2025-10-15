package com.zwstudio.logicpuzzlesandroid.puzzles.numbercrosswords

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class NumberCrosswordsGameState(game: NumberCrosswordsGame) : CellsGameState<NumberCrosswordsGame, NumberCrosswordsGameMove, NumberCrosswordsGameState>(game) {
    val objArray = Array(rows * cols) { NumberCrosswordsObject.Normal }
    var row2state = Array(rows - 1) { HintState.Normal }
    var col2state = Array(cols - 1) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: NumberCrosswordsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: NumberCrosswordsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: NumberCrosswordsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: NumberCrosswordsGameMove): GameOperationType {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        fun f(obj: NumberCrosswordsObject) =
            when (obj) {
                NumberCrosswordsObject.Normal ->
                    if (markerOption == MarkerOptions.MarkerFirst) NumberCrosswordsObject.Marker
                    else NumberCrosswordsObject.Darken
                NumberCrosswordsObject.Darken ->
                    if (markerOption == MarkerOptions.MarkerLast) NumberCrosswordsObject.Marker
                    else NumberCrosswordsObject.Normal
                NumberCrosswordsObject.Marker ->
                    if (markerOption == MarkerOptions.MarkerFirst) NumberCrosswordsObject.Darken
                    else NumberCrosswordsObject.Normal
            }
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        move.obj = f(this[p])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games 2/Puzzle Set 1/Number Crosswords

        Summary
        More crosswords for Robots

        Description
        1. Blacken some tiles, so that some of the numbers remain visible.
        2. Numbers outside the grid show the states of the numbers in the
           remaining tiles in that row or column.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 1. Blacken some tiles, so that some of the numbers remain visible.
        // 2. Numbers outside the grid show the states of the numbers in the
        //    remaining tiles in that row or column.
        for (r in 0 until rows - 1) {
            var sum = 0
            for (c in 0 until cols - 1) {
                val p = Position(r, c)
                if (this[p] == NumberCrosswordsObject.Darken) continue
                sum += game[p]
            }
            val n = game[r, cols - 1]
            val s = if (sum > n) HintState.Normal else if (sum == n) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 1. Blacken some tiles, so that some of the numbers remain visible.
        // 2. Numbers outside the grid show the states of the numbers in the
        //    remaining tiles in that row or column.
        for (c in 0 until cols - 1) {
            var sum = 0
            for (r in 0 until rows - 1) {
                val p = Position(r, c)
                if (this[p] == NumberCrosswordsObject.Darken) continue
                sum += game[p]
            }
            val n = game[rows - 1, c]
            val s = if (sum > n) HintState.Normal else if (sum == n) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}