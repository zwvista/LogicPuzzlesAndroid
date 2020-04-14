package com.zwstudio.logicpuzzlesandroid.puzzles.calcudoku.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import fj.Ord
import fj.data.Set
import fj.data.Stream
import fj.function.Integers
import java.util.*

class CalcudokuGameState(game: CalcudokuGame?) : CellsGameState<CalcudokuGame?, CalcudokuGameMove?, CalcudokuGameState?>(game) {
    var objArray: IntArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    var pos2state: MutableMap<Position?, HintState?> = HashMap()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position?) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Int) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position?, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: CalcudokuGameMove): Boolean {
        if (!isValid(move!!.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: CalcudokuGameMove): Boolean {
        val p = move!!.p
        if (!isValid(p)) return false
        val o = get(p)
        move.obj = (o + 1) % (cols() + 1)
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
        val f = F { nums: List<Int> ->
            val nums2 = Set.iterableSet(Ord.intOrd, nums).toJavaList()
            // 1. Write numbers ranging from 1 to board size.
            val s = if (nums2[0] == 0) HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        // 6. All the numbers appear just one time in each row.
        Stream.range(0, rows().toLong()).foreachDoEffect { r: Int -> row2state[r] = f.f(Stream.range(0, cols().toLong()).map { c: Int -> get(r, c) }.toJavaList()) }
        // 6. All the numbers appear just one time in each column.
        Stream.range(0, cols().toLong()).foreachDoEffect { c: Int -> col2state[c] = f.f(Stream.range(0, rows().toLong()).map { r: Int -> get(r, c) }.toJavaList()) }
        for ((p, h) in game!!.pos2hint) {
            val nums = fj.data.List.iterableList(game!!.areas[game!!.pos2area[p]!!]).map { p2: Position? -> get(p2) }.toJavaList()
            val g = label@ F0 {
                if (nums.contains(0)) return@label HintState.Normal
                val n = h.result
                when (h.op) {
                    '+' -> return@label if (fj.data.List.iterableList(nums).foldLeft(Integers.add, 0) == n) HintState.Complete else HintState.Error
                    '-' -> {
                        val n1 = nums[0]
                        val n2 = nums[1]
                        return@label if (n1 - n2 == n || n2 - n1 == n) HintState.Complete else HintState.Error
                    }
                    '*' -> return@label if (fj.data.List.iterableList(nums).foldLeft(Integers.multiply, 1) == n) HintState.Complete else HintState.Error
                    '/' -> {
                        val n1 = nums[0]
                        val n2 = nums[1]
                        return@label if (n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1) HintState.Complete else HintState.Error
                    }
                }
                HintState.Normal
            }
            val s = g.f()
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = IntArray(rows() * cols())
        row2state = arrayOfNulls(rows())
        col2state = arrayOfNulls(cols())
        updateIsSolved()
    }
}