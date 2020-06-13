package com.zwstudio.logicpuzzlesandroid.puzzles.square100

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class Square100GameState(game: Square100Game) : CellsGameState<Square100Game, Square100GameMove, Square100GameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2hint = IntArray(rows)
    var col2hint = IntArray(cols)

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: String) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: String) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: Square100GameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: Square100GameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val n = this[p]
        var o = n[if (move.isRightPart) 2 else 0]
        o = when (o) {
            ' ' -> if (markerOption == MarkerOptions.MarkerFirst) '.' else '0'
            '.' -> if (markerOption == MarkerOptions.MarkerFirst) '0' else ' '
            '9' -> if (markerOption == MarkerOptions.MarkerLast) '.' else ' '
            else -> o + 1
        }
        if (!move.isRightPart && o == '0')
            o = '1'
        move.obj = if (move.isRightPart) n.substring(0, 2) + o else o + n.substring(1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 10/Square100

        Summary
        Exactly one hundred

        Description
        1. You are given a 3*3 or 4*4 square with digits in it.
        2. You have to add digits to some (or all) tiles, in order to produce
           the sum of 100 for every row and column.
        3. You can add digits before or after the given one.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(r: Int, c: Int): Int {
            val o = this[r, c]
            var n = o[1] - '0'
            // 3. You can add digits before or after the given one.
            if (Character.isDigit(o[0])) n += (o[0] - '0') * 10
            if (Character.isDigit(o[2])) n = n * 10 + (o[2] - '0')
            return n
        }
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every row.
        for (r in 0 until rows) {
            var n = 0
            for (c in 0 until cols)
                n += f(r, c)
            row2hint[r] = n
            if (n != 100) isSolved = false
        }
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every column.
        for (c in 0 until cols) {
            var n = 0
            for (r in 0 until rows)
                n += f(r, c)
            col2hint[c] = n
            if (n != 100) isSolved = false
        }
    }
}