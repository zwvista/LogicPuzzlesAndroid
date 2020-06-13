package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MathraxGameState(game: MathraxGame) : CellsGameState<MathraxGame, MathraxGameMove, MathraxGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Int) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    fun setObject(move: MathraxGameMove): Boolean {
        if (!isValid(move.p) || game[move.p] != 0 || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: MathraxGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || game[p] != 0) return false
        val o = this[p]
        move.obj = (o + 1) % (cols + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Mathrax

        Summary
        Diagonal Math Wiz

        Description
        1. The goal is to input numbers 1 to N, where N is the board size, following
           the hints in the intersections.
        2. A number must appear once for every row and column.
        3. The tiny numbers and sign in the intersections tell you the result of
           the operation between the two opposite diagonal tiles. This is valid
           for both pairs of numbers surrounding the hint.
        4. In some puzzles, there will be 'E' or 'O' as hint. This means that all
           four tiles are either (E)ven or (O)dd numbers.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(nums: List<Int>): HintState {
            val size = nums.size
            val nums2 = nums.toSet().toList()
            // 1. The goal is to input numbers 1 to N, where N is the board size.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 2. A number must appear once for every row.
        for (r in 0 until rows)
            row2state[r] = f((0 until cols).map { this[r, it] })
        // 2. A number must appear once for every column.
        for (c in 0 until cols)
            col2state[c] = f((0 until rows).map { this[it, c] })
        for ((p, h) in game.pos2hint.entries) {
            fun g(n1: Int, n2: Int): HintState {
                if (n1 == 0 || n2 == 0) return HintState.Normal
                val n = h.result
                when (h.op) {
                    '+' -> return if (n1 + n2 == n) HintState.Complete else HintState.Error
                    '-' -> return if (n1 - n2 == n || n2 - n1 == n) HintState.Complete else HintState.Error
                    '*' -> return if (n1 * n2 == n) HintState.Complete else HintState.Error
                    '/' -> return if (n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1) HintState.Complete else HintState.Error
                    'O' -> return if (n1 % 2 == 1 && n2 % 2 == 1) HintState.Complete else HintState.Error
                    'E' -> return if (n1 % 2 == 0 && n2 % 2 == 0) HintState.Complete else HintState.Error
                }
                return HintState.Normal
            }
            val nums = MathraxGame.offset2.map { this[p + it] }
            // 3. This is valid for both pairs of numbers surrounding the hint.
            val s1 = g(nums[0], nums[1])
            val s2 = g(nums[2], nums[3])
            val s = if (s1 == HintState.Error || s2 == HintState.Error) HintState.Error else if (s1 == HintState.Complete && s2 == HintState.Complete) HintState.Complete else HintState.Normal
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}