package com.zwstudio.logicpuzzlesandroid.puzzles.tatami

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class TatamiGameState(game: TatamiGame) : CellsGameState<TatamiGame, TatamiGameMove, TatamiGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: TatamiGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: TatamiGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return false
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Tatami

        Summary
        1,2,3... 1,2,3... Fill the mats

        Description
        1. Each rectangle represents a mat(Tatami) which is of the same size.
           You must fill each Tatami with a number ranging from 1 to size.
        2. Each number can appear only once in each Tatami.
        3. In one row or column, each number must appear the same number of times.
        4. You can't have two identical numbers touching horizontally or vertically.
    */
    private fun updateIsSolved() {
        isSolved = true
        val chars2 = listOf('1', '2', '3')
        val chars3 = chars2.flatMap { Array(rows / 3) { it }.toList() }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == ' ') isSolved = false
                pos2state[p] = HintState.Normal
            }
        for (r in 0 until rows) {
            var lineSolved = true
            for (c in 0 until cols - 1) {
                val p1 = Position(r, c)
                val p2 = Position(r, c + 1)
                val ch1 = this[p1]
                val ch2 = this[p2]
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    // 4. You can't have two identical numbers touching horizontally.
                    lineSolved = false
                    isSolved = lineSolved
                    pos2state[p1] = HintState.Error
                    pos2state[p2] = HintState.Error
                }
            }
            val chars = (0 until cols).map { this[r, it] }.sorted()
            // 3. In one row, each number must appear the same number of times.
            if (chars[0] != ' ' && chars != chars3) {
                lineSolved = false
                isSolved = lineSolved
                for (c in 0 until cols)
                    pos2state[Position(r, c)] = HintState.Error
            }
            if (lineSolved)
                for (c in 0 until cols)
                    pos2state[Position(r, c)] = HintState.Complete
        }
        for (c in 0 until cols) {
            var lineSolved = true
            for (r in 0 until rows - 1) {
                val p1 = Position(r, c)
                val p2 = Position(r + 1, c)
                val ch1 = this[p1]
                val ch2 = this[p2]
                if (ch1 != ' ' && ch2 != ' ' && ch1 == ch2) {
                    // 4. You can't have two identical numbers touching vertically.
                    lineSolved = false
                    isSolved = lineSolved
                    pos2state[p1] = HintState.Error
                    pos2state[p2] = HintState.Error
                }
            }
            val chars = (0 until rows).map { this[it, c] }.sorted()
            // 3. In one column, each number must appear the same number of times.
            if (chars[0] != ' ' && chars != chars3) {
                lineSolved = false
                isSolved = lineSolved
                for (r in 0 until rows)
                    pos2state[Position(r, c)] = HintState.Error
            }
            if (lineSolved)
                for (r in 0 until rows)
                    pos2state[Position(r, c)] = HintState.Complete
        }
        // 2. Each number can appear only once in each Tatami.
        for (a in game.areas) {
            val chars = a.map { this[it] }.sorted()
            if (chars[0] != ' ' && chars != chars2) {
                isSolved = false
                for (p in a)
                    pos2state[p] = HintState.Error
            }
        }
    }
}