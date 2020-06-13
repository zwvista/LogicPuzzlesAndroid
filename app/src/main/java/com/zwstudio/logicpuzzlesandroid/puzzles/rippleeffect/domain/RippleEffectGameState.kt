package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class RippleEffectGameState(game: RippleEffectGame) : CellsGameState<RippleEffectGame, RippleEffectGameMove, RippleEffectGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: RippleEffectGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0 || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: RippleEffectGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return false
        move.obj = (this[p] + 1) % (game.areas[game.pos2area[p]!!].size + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Ripple Effect

        Summary
        Fill the Room with the numbers, but take effect of the Ripple Effect

        Description
        1. The goal is to fill the Rooms you see on the board, with numbers 1 to room size.
        2. While doing this, you must consider the Ripple Effect. The same number
           can only appear on the same row or column at the distance of the number
           itself.
        3. For example a 2 must be separated by another 2 on the same row or
           column by at least two tiles.
    */
    private fun updateIsSolved() {
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                pos2state[Position(r, c)] = HintState.Normal
        val num2rng = mutableMapOf<Int, MutableList<Position>>()
        fun f(sameRow: Boolean) {
            for ((n, rng) in num2rng) {
                val indexes = mutableSetOf<Int>()
                for (i in 0 until rng.size - 1)
                    if (if (sameRow) rng[i + 1].col - rng[i].col <= n else rng[i + 1].row - rng[i].row <= n) {
                    indexes.add(n)
                    indexes.add(n + 1)
                }
                if (indexes.isNotEmpty()) isSolved = false
                for (i in rng.indices)
                    if (indexes.contains(i))
                        pos2state[rng[i]] = HintState.Error
            }
        }
        for (r in 0 until rows) {
            num2rng.clear()
            for (c in 0 until cols) {
                val p = Position(r, c)
                val n = get(p)
                if (n == 0) {
                    isSolved = false
                    continue
                }
                val rng = num2rng[n] ?: mutableListOf()
                rng.add(p)
                num2rng[n] = rng
            }
            f(true)
        }
        for (c in 0 until cols) {
            num2rng.clear()
            for (r in 0 until rows) {
                val p = Position(r, c)
                val n = get(p)
                if (n == 0) {
                    isSolved = false
                    continue
                }
                val rng = num2rng[n] ?: mutableListOf()
                rng.add(p)
                num2rng[n] = rng
            }
            f(false)
        }
        for (area in game.areas) {
            num2rng.clear()
            for (p in area) {
                val n = this[p]
                if (n == 0) continue
                var rng = num2rng[n] ?: mutableListOf()
                rng.add(p)
                num2rng[n] = rng
            }
            var anySame = false
            for (rng in num2rng.values) {
                if (rng.size <= 1) continue
                anySame = true
                isSolved = false
                for (p in rng)
                    pos2state[p] = HintState.Error
            }
            if (!anySame)
                for (p in area)
                    if (pos2state[p] != HintState.Error)
                        pos2state[p] = HintState.Complete
        }
    }
}