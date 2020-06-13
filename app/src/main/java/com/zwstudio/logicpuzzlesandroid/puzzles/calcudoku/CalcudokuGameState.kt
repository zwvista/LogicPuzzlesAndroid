package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class CalcudokuGameState(game: CalcudokuGame) : CellsGameState<CalcudokuGame, CalcudokuGameMove, CalcudokuGameState>(game) {
    var objArray = IntArray(rows * cols)
    var row2state = Array<HintState>(rows) { HintState.Normal }
    var col2state = Array<HintState>(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Int) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: CalcudokuGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: CalcudokuGameMove): Boolean {
        val p = move.p
        if (!isValid(p)) return false
        val o = get(p)
        move.obj = (o + 1) % (cols + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 4/Calcudoku

        Summary
        Mathematical Sudoku

        Description
        1. Write numbers ranging from 1 to board size respecting the calculation
           hint.
        2. The tiny numbers and math signs in the corner of an area give you the
           hint about what's happening inside that area.
        3. For example a '3+' means that the sum of the numbers inside that area
           equals 3. In that case you would have to write the numbers 1 and 2
           there.
        4. Another example: '12*' means that the multiplication of the numbers
           in that area gives 12, so it could be 3 and 4 or even 3, 4 and 1,
           depending on the area size.
        5. Even where the order of the operands matter (in subtraction and division)
           they can appear in any order inside the area (ie.e. 2/ could be done
           with 4 and 2 or 2 and 4.
        6. All the numbers appear just one time in each row and column, but they
           could be repeated in non-straight areas, like the L-shaped one.
    */
    private fun updateIsSolved() {
        isSolved = true
        fun f(nums: List<Int>): HintState {
            val nums2 = nums.toSortedSet()
            // 1. Write numbers ranging from 1 to board size.
            val s = if (nums2.first() == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 6. All the numbers appear just one time in each row.
        for (r in 0 until rows)
            row2state[r] = f((0 until cols).map { this[r, it] })
        // 6. All the numbers appear just one time in each column.
        for (c in 0 until cols)
            col2state[c] = f((0 until rows).map { this[it, c] })
        for ((p, h) in game.pos2hint) {
            val nums = game.areas[game.pos2area[p]!!].map { this[it] }
            fun g(): HintState {
                if (nums.contains(0)) return HintState.Normal
                val n = h.result
                return when (h.op) {
                    '+' -> if (nums.sum() == n) HintState.Complete else HintState.Error
                    '-' -> {
                        val n1 = nums[0]
                        val n2 = nums[1]
                        if (n1 - n2 == n || n2 - n1 == n) HintState.Complete else HintState.Error
                    }
                    '*' -> if (nums.fold(1) { acc, v -> acc * v } == n) HintState.Complete else HintState.Error
                    '/' -> {
                        val n1 = nums[0]
                        val n2 = nums[1]
                        if (n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1) HintState.Complete else HintState.Error
                    }
                    else -> HintState.Normal
                }
            }
            val s = g()
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}