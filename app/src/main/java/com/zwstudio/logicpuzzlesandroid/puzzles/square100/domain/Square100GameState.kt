package com.zwstudio.logicpuzzlesandroid.puzzles.square100.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2

class Square100GameState(game: Square100Game) : CellsGameState<Square100Game, Square100GameMove, Square100GameState>(game) {
    var objArray: Array<String?>
    var row2hint: IntArray
    var col2hint: IntArray
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: String?) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: String?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: Square100GameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: Square100GameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val p: Position = move.p
        if (!isValid(p)) return false
        val n = get(p)
        var o = n!![if (move.isRightPart) 2 else 0]
        o = if (o == ' ') if (markerOption == MarkerOptions.MarkerFirst) '.' else '0' else if (o == '.') if (markerOption == MarkerOptions.MarkerFirst) '0' else ' ' else if (o == '9') if (markerOption == MarkerOptions.MarkerLast) '.' else ' ' else (o.toInt() + 1).toChar()
        if (!move.isRightPart && o == '0') o = '1'
        move.obj = if (move.isRightPart) n.substring(0, 2) + o else o.toString() + n.substring(1)
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
        val f: F2<Int, Int, Int> = F2<Int, Int, Int> { r: Int, c: Int ->
            val o = get(r, c)
            var n = o!![1] - '0'
            // 3. You can add digits before or after the given one.
            if (Character.isDigit(o[0])) n += (o[0] - '0') * 10
            if (Character.isDigit(o[2])) n = n * 10 + (o[2] - '0')
            n
        }
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every row.
        for (r in 0 until rows()) {
            var n = 0
            for (c in 0 until cols()) n += f.f(r, c)
            row2hint[r] = n
            if (n != 100) isSolved = false
        }
        // 2. You have to add digits to some (or all) tiles, in order to produce
        // the sum of 100 for every column.
        for (c in 0 until cols()) {
            var n = 0
            for (r in 0 until rows()) n += f.f(r, c)
            col2hint[c] = n
            if (n != 100) isSolved = false
        }
    }

    init {
        objArray = arrayOfNulls(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2hint = IntArray(rows())
        col2hint = IntArray(cols())
        updateIsSolved()
    }
}