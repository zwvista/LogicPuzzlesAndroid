package com.zwstudio.logicpuzzlesandroid.puzzles.digitworms

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class DigitWormsGameState(game: DigitWormsGame) : CellsGameState<DigitWormsGame, DigitWormsGameMove, DigitWormsGameState>(game) {
    val objArray = game.objArray.copyOf()
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: DigitWormsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != DigitWormsGame.PUZ_EMPTY || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: DigitWormsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != DigitWormsGame.PUZ_EMPTY) return GameOperationType.Invalid
        val o = this[p]
        move.obj = if (o == game.areas[game.pos2area[p]!!].size) DigitWormsGame.PUZ_EMPTY else o + 1
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 1/Digit Worms

        Summary
        Or a hand of worms

        Description
        1. Fill each area with numbers from 1 to the area size, putting them like
           a snake, or worm, in succession.
        2. No number must be orthogonally or diagonally touching the same number
           from another area.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                pos2state[Position(r, c)] = HintState.Normal
        // 2. No number must be orthogonally or diagonally touching the same number
        //    from another area.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val n = this[p]
                if (n == DigitWormsGame.PUZ_EMPTY) { isSolved = false; continue }
                val rng = DigitWormsGame.offset.map { p + it }.filter { isValid(it) && this[it] == n }
                if (rng.isEmpty()) continue
                isSolved = false
                for (p2 in listOf(p) + rng) { pos2state[p2] = HintState.Error }
            }
        next@ for (area in game.areas) {
            val num2rng = mutableMapOf<Int, MutableList<Position>>()
            for (p in area) {
                val n = this[p]
                if (n == DigitWormsGame.PUZ_EMPTY) continue@next
                num2rng.getOrPut(n) { mutableListOf<Position>() }.add(p)
            }
            val s = if (num2rng.size == area.size && (1..<area.size).all { i ->
                DigitWormsGame.offset.contains((num2rng[i + 1]!![0] - num2rng[i]!![0]))
            }) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            for ((_, rng) in num2rng)
                for (p in rng)
                    if (pos2state[p] != HintState.Error)
                        pos2state[p] = s
        }
    }
}