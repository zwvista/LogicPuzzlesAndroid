package com.zwstudio.logicpuzzlesandroid.puzzles.snail.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.*

class SnailGameState(game: SnailGame) : CellsGameState<SnailGame, SnailGameMove, SnailGameState>(game) {
    private val objArray: CharArray
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, obj: Char) {
        objArray[row * cols() + col] = obj
    }

    operator fun set(p: Position, obj: Char) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: SnailGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || get(p) == move.obj) return false
        set(p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: SnailGameMove): Boolean {
        val markerOption: MarkerOptions = MarkerOptions.values().get(game.gdi.getMarkerOption())
        val p: Position = move.p
        if (!isValid(p)) return false
        val o = get(p)
        move.obj = if (o == ' ') if (markerOption == MarkerOptions.MarkerFirst) '.' else '1' else if (o == '.') if (markerOption == MarkerOptions.MarkerFirst) '1' else ' ' else if (o == '3') if (markerOption == MarkerOptions.MarkerLast) '.' else ' ' else (o.toInt() + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Snail

        Summary
        Darken some tiles so no number appears in a row or column more than once

        Description
        1. The goal is to shade squares so that a number appears only once in a
           row or column.
        2. While doing that, you must take care that shaded squares don't touch
           horizontally or vertically between them.
        3. In the end all the un-shaded squares must form a single continuous area.
    */
    private fun updateIsSolved() {
        isSolved = true
        var chars: String
        // 5. Board Rule: Each row of the board (disregarding the snail
        // path) must have exactly one 1, one 2 and one 3.
        for (r in 0 until rows()) {
            chars = ""
            row2state[r] = HintState.Complete
            for (c in 0 until cols()) {
                val ch = get(r, c)
                if (ch == ' ') continue
                chars += if (chars.contains(ch.toString())) break else ch
            }
            if (chars.length != 3) {
                row2state[r] = HintState.Error
                isSolved = false
            }
        }
        // 5. Board Rule: Each column of the board (disregarding the snail
        // path) must have exactly one 1, one 2 and one 3.
        for (c in 0 until cols()) {
            chars = ""
            col2state[c] = HintState.Complete
            for (r in 0 until rows()) {
                val ch = get(r, c)
                if (ch == ' ') continue
                chars += if (chars.contains(ch.toString())) break else ch
            }
            if (chars.length != 3) {
                col2state[c] = HintState.Error
                isSolved = false
            }
        }
        val rng = mutableListOf<Position>()
        chars = ""
        for (p in game.snailPathGrid) {
            val ch = get(p)
            pos2state[p] = HintState.Error
            if (ch == ' ') continue
            rng.add(p)
            chars += ch
            pos2state[p] = HintState.Complete
        }
        val cnt = chars.length
        // 4. Trail Rule: The first number to write after entering in the top left
        // is a 1 and the last before ending in the center is a 3. In between,
        // the 1,2,3 sequence will repeat many times in this order, following the
        // snail path.
        if (chars[0] != '1') {
            pos2state[rng[0]] = HintState.Error
            isSolved = false
        }
        if (chars[cnt - 1] != '3') {
            pos2state[rng[cnt - 1]] = HintState.Error
            isSolved = false
        }
        for (i in 0 until cnt - 1) {
            val ch1 = chars[i]
            val ch2 = chars[i + 1]
            if (!(ch1 == '1' && ch2 == '2' || ch1 == '2' && ch2 == '3' || ch1 == '3' && ch2 == '1')) {
                pos2state[rng[i]] = HintState.Error
                isSolved = false
            }
        }
    }

    init {
        objArray = CharArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}