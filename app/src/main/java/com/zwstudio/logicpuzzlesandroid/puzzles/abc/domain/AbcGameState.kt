package com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class AbcGameState(game: AbcGame) : CellsGameState<AbcGame, AbcGameMove, AbcGameState>(game) {
    private val objArray = game.objArray
    var row2state = Array(rows() * 2) { HintState.Normal }
    var col2state = Array(cols() * 2) { HintState.Normal }

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    fun getState(row: Int, col: Int) = when {
        row == 0 && col in 1 until cols() - 1 -> col2state[col * 2]
        row == rows() - 1 && col in 1 until cols() - 1 -> col2state[col * 2 + 1]
        col == 0 && row in 1 until rows() - 1 -> row2state[row * 2]
        col == cols() - 1 && row in 1 until rows() - 1 -> row2state[row * 2 + 1]
        else -> HintState.Normal
    }

    fun setObject(move: AbcGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: AbcGameMove): Boolean {
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        move.obj = when(o) {
            ' ' -> if (markerOption == MarkerOptions.MarkerFirst) '.' else 'A'
            '.' -> if (markerOption == MarkerOptions.MarkerFirst) 'A' else ' '
            game.chMax -> if (markerOption == MarkerOptions.MarkerLast) '.' else ' '
            else -> (o.toInt() + 1).toChar()
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 1/Abc

        Summary
        Fill the board with ABC

        Description
        1. The goal is to put the letter A B and C in the board.
        2. Each letter appears once in every row and column.
        3. The letters on the borders tell you what letter you see from there.
        4. Since most puzzles can contain empty spaces, the hint on the board
           doesn't always match the tile next to it.
        5. Bigger puzzles can contain the letter 'D'. In these cases, the name
           of the puzzle is 'Abcd'. Further on, you might also encounter 'E',
           'F' etc.
    */
    private fun updateIsSolved() {
        isSolved = true
        val chars = mutableListOf<Char>()
        for (r in 1 until rows() - 1) {
            val (h1, h2) = listOf(this[r, 0], this[r, cols() - 1])
            var (ch11, ch21) = listOf(' ', ' ')
            chars.clear()
            for (c in 1 until cols() - 1) {
                val (ch12, ch22) = listOf(this[r, c], this[r, cols() - 1 - c])
                if (ch11 == ' ' && ch12 != ' ') ch11 = ch12
                if (ch21 == ' ' && ch22 != ' ') ch21 = ch22
                if (ch12 == ' ') continue
                // 2. Each letter appears once in every row.
                if (chars.contains(ch12))
                    isSolved = false
                else
                    chars.add(ch12)
            }
            // 3. The letters on the borders tell you what letter you see from there.
            val s1 = if (ch11 == ' ') HintState.Normal else if (ch11 == h1) HintState.Complete else HintState.Error
            val s2 = if (ch21 == ' ') HintState.Normal else if (ch21 == h2) HintState.Complete else HintState.Error
            row2state[r * 2] = s1; row2state[r * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            // 2. Each letter appears once in every row.
            if (chars.size != game.chMax - 'A' + 1) isSolved = false
        }
        for (c in 1 until cols() - 1) {
            val (h1, h2) = listOf(this[0, c], this[rows() - 1, c])
            var (ch11, ch21) = listOf(' ', ' ')
            chars.clear()
            for (r in 1 until rows() - 1) {
                val (ch12, ch22) = listOf(this[r, c], this[rows() - 1 - r, c])
                if (ch11 == ' ' && ch12 != ' ') ch11 = ch12
                if (ch21 == ' ' && ch22 != ' ') ch21 = ch22
                if (ch12 == ' ') continue
                // 2. Each letter appears once in every column.
                if (chars.contains(ch12))
                    isSolved = false
                else
                    chars.add(ch12)
            }
            // 3. The letters on the borders tell you what letter you see from there.
            val s1 = if (ch11 == ' ') HintState.Normal else if (ch11 == h1) HintState.Complete else HintState.Error
            val s2 = if (ch21 == ' ') HintState.Normal else if (ch21 == h2) HintState.Complete else HintState.Error
            col2state[c * 2] = s1; col2state[c * 2 + 1] = s2
            if (s1 != HintState.Complete || s2 != HintState.Complete) isSolved = false
            // 2. Each letter appears once in every column.
            if (chars.size != game.chMax - 'A' + 1) isSolved = false
        }
    }
}
