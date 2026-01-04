package com.zwstudio.logicpuzzlesandroid.puzzles.chocolate

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ChocolateGameState(game: ChocolateGame) : CellsGameState<ChocolateGame, ChocolateGameMove, ChocolateGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Char) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: ChocolateGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ' || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ChocolateGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != ' ') return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == ' ') '1' else if (o == '3') ' ' else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Chocolate

        Summary
        Yummy!

        Description
        1. Find some chocolate bars following these rules:
        2. A Chocolate bar is a rectangular or a square.
        3. Chocolate tiles form bars independently of the area borders.
        4. Chocolate bars must not be orthogonally adjacent.
        5. A tile with a number indicates how many tiles in the area must
           be chocolate.
        6. An area without number can have any number of tiles of chocolate.
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
        // 2. Each number can appear only once in each Chocolate.
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