package com.zwstudio.logicpuzzlesandroid.puzzles.inbetweensumscrapers

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class InbetweenSumscrapersGameState(game: InbetweenSumscrapersGame) : CellsGameState<InbetweenSumscrapersGame, InbetweenSumscrapersGameMove, InbetweenSumscrapersGameState>(game) {
    val objArray = IntArray(rows * cols)
    val row2state = Array(rows) { HintState.Normal }
    val col2state = Array(cols) { HintState.Normal }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: InbetweenSumscrapersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: InbetweenSumscrapersGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val o = get(p)
        // 3. The remaining cells contain numbers increasing from 1 to N-2 (N being
        //    the board size).
        move.obj = if (o == rows - 2) InbetweenSumscrapersGame.PUZ_SKYSCRAPER else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 6/Inbetween Sumscrapers

        Summary
        Sumscrapers on the ground

        Description
        1. Find two Skyscrapers and fill the remaining cells with numbers.
        2. Each row and column contains two skyscrapers.
        3. The remaining cells contain numbers increasing from 1 to N-2 (N being
           the board size).
        4. Numbers appear once in every row and column.
        5. Hints on the border give you the sums of the numbers between the skyscrapers.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = AllowedObjectState.Normal
        for (r in 0..<rows) {
            val skyscrapers = mutableListOf<Position>()
            val num2rng = mutableMapOf<Int, MutableList<Position>>()
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (val n = this[p]) {
                    InbetweenSumscrapersGame.PUZ_SKYSCRAPER -> skyscrapers.add(p)
                    InbetweenSumscrapersGame.PUZ_EMPTY -> isSolved = false
                    else -> num2rng.getOrPut(n) { mutableListOf() }.add(p)
                }
            }
            for ((_, rng) in num2rng) {
                val cnt = rng.size
                if (cnt < 2) continue
                isSolved = false
                for (p in rng) pos2state[p] = AllowedObjectState.Error
            }
            val n1 = skyscrapers.size
            val n2 = game.row2hint[r]
            // 2. Each row and column contains two skyscrapers.
            if (n1 > 2)
                for (p in skyscrapers)
                    pos2state[p] = AllowedObjectState.Error
            if (n2 == InbetweenSumscrapersGame.PUZ_UNKNOWN) continue
            // 3. The remaining cells contain numbers increasing from 1 to N-2 (N being
            //    the board size).
            // 4. Numbers appear once in every row and column.
            // 5. Hints on the border give you the sums of the numbers between the skyscrapers.
            val s = if (n1 < 2) HintState.Normal else
                if (n1 == 2 && n2 == (skyscrapers[0].col + 1..<skyscrapers[1].col).fold(0) {
                    acc, c -> acc + this[r, c] }) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 0..<cols) {
            val skyscrapers = mutableListOf<Position>()
            val num2rng = mutableMapOf<Int, MutableList<Position>>()
            for (r in 0..<rows) {
                val p = Position(r, c)
                when (val n = this[p]) {
                    InbetweenSumscrapersGame.PUZ_SKYSCRAPER -> skyscrapers.add(p)
                    InbetweenSumscrapersGame.PUZ_EMPTY -> isSolved = false
                    else -> num2rng.getOrPut(n) { mutableListOf() }.add(p)
                }
            }
            for ((_, rng) in num2rng) {
                val cnt = rng.size
                if (cnt < 2) continue
                isSolved = false
                for (p in rng) pos2state[p] = AllowedObjectState.Error
            }
            val n1 = skyscrapers.size
            val n2 = game.col2hint[c]
            // 2. Each row and column contains two skyscrapers.
            if (n1 > 2)
                for (p in skyscrapers)
                    pos2state[p] = AllowedObjectState.Error
            if (n2 == InbetweenSumscrapersGame.PUZ_UNKNOWN) continue
            // 3. The remaining cells contain numbers increasing from 1 to N-2 (N being
            //    the board size).
            // 4. Numbers appear once in every row and column.
            // 5. Hints on the border give you the sums of the numbers between the skyscrapers.
            val s = if (n1 < 2) HintState.Normal else
                if (n1 == 2 && n2 == (skyscrapers[0].row + 1..<skyscrapers[1].row).fold(0) {
                    acc, r -> acc + this[r, c] }) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}