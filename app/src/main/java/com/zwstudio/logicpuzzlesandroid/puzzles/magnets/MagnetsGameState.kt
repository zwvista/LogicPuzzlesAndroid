package com.zwstudio.logicpuzzlesandroid.puzzles.magnets

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class MagnetsGameState(game: MagnetsGame) : CellsGameState<MagnetsGame, MagnetsGameMove, MagnetsGameState>(game) {
    var objArray = Array(rows * cols) { MagnetsObject.Empty }
    var row2state = Array(rows * 2) { HintState.Normal }
    var col2state = Array(cols * 2) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MagnetsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MagnetsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: MagnetsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game.singles.contains(p) || this[p] === move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: MagnetsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            MagnetsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MagnetsObject.Marker else MagnetsObject.Positive
            MagnetsObject.Positive -> MagnetsObject.Negative
            MagnetsObject.Negative -> if (markerOption == MarkerOptions.MarkerLast) MagnetsObject.Marker else MagnetsObject.Empty
            MagnetsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MagnetsObject.Positive else MagnetsObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Magnets

        Summary
        Place Magnets on the board, respecting the orientation of poles

        Description
        1. Each Magnet has a positive(+) and a negative(-) pole.
        2. Every rectangle can either contain a Magnet or be empty.
        3. The number on the board tells you how many positive and negative poles
           you can see from there in a straight line.
        4. When placing a Magnet, you have to respect the rule that the same pole
           (+ and + / - and -) can't be adjacent horizontally or vertically.
        5. In some levels, a few numbers on the border can be hidden.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 3. The number on the board tells you how many positive and negative poles
        // you can see from there in a straight line.
        for (r in 0 until rows) {
            var np1 = 0
            val np2 = game.row2hint[r * 2]
            var nn1 = 0
            val nn2 = game.row2hint[r * 2 + 1]
            for (c in 0 until cols)
                when (this[r, c]) {
                    MagnetsObject.Positive -> np1++
                    MagnetsObject.Negative -> nn1++
                    else -> {}
                }
            row2state[r * 2] = if (np1 < np2) HintState.Normal else if (np1 == np2) HintState.Complete else HintState.Error
            row2state[r * 2 + 1] = if (nn1 < nn2) HintState.Normal else if (nn1 == nn2) HintState.Complete else HintState.Error
            if (np1 != np2 || nn1 != nn2) isSolved = false
        }
        // 3. The number on the board tells you how many positive and negative poles
        // you can see from there in a straight line.
        for (c in 0 until cols) {
            var np1 = 0
            val np2 = game.col2hint[c * 2]
            var nn1 = 0
            val nn2 = game.col2hint[c * 2 + 1]
            for (r in 0 until rows)
                when (this[r, c]) {
                    MagnetsObject.Positive -> np1++
                    MagnetsObject.Negative -> nn1++
                    else -> {}
                }
            col2state[c * 2] = if (np1 < np2) HintState.Normal else if (np1 == np2) HintState.Complete else HintState.Error
            col2state[c * 2 + 1] = if (nn1 < nn2) HintState.Normal else if (nn1 == nn2) HintState.Complete else HintState.Error
            if (np1 != np2 || nn1 != nn2) isSolved = false
        }
        if (!isSolved) return
        // 2. Every rectangle can either contain a Magnet or be empty.
        loop@ for (a in game.areas)
            when (a.type) {
                MagnetsAreaType.Single -> continue@loop
                MagnetsAreaType.Horizontal, MagnetsAreaType.Vertical -> {
                    val os = MagnetsGame.offset[if (a.type == MagnetsAreaType.Horizontal) 1 else 2]
                    val o1 = this[a.p]
                    val o2 = this[a.p + os]
                    if (o1.isEmpty != o2.isEmpty) {
                        isSolved = false
                        return
                    }
                }
            }
        // 1. Each Magnet has a positive(+) and a negative(-) pole.
        // 4. When placing a Magnet, you have to respect the rule that the same pole
        // (+ and + / - and -) can't be adjacent horizontally or vertically.
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[r, c]
                for (os in MagnetsGame.offset) {
                    val p2 = p + os
                    if (!isValid(p2)) continue
                    val o2 = this[p2]
                    if (o.isPole && o === o2) {
                        isSolved = false
                        return
                    }
                }
            }
    }
}