package com.zwstudio.logicpuzzlesandroid.puzzles.mathrax.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.data.Set
import fj.data.Stream
import java.util.*

class MathraxGameState(game: MathraxGame) : CellsGameState<MathraxGame, MathraxGameMove, MathraxGameState>(game) {
    var objArray: IntArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    var pos2state: MutableMap<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Int) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Int) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: MathraxGameMove): Boolean {
        if (!isValid(move.p) || game.get(move.p) != 0 || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: MathraxGameMove): Boolean {
        val p: Position = move.p
        if (!isValid(p) || game.get(p) != 0) return false
        val o = get(p)
        move.obj = (o + 1) % (cols() + 1)
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
        val f: F<List<Int>, HintState> = F<List<Int>, HintState> { nums: List<Int> ->
            val size = nums.size
            val nums2 = Set.iterableSet(Ord.intOrd, nums).toJavaList()
            // 1. The goal is to input numbers 1 to N, where N is the board size.
            val s: HintState = if (nums2[0] == 0) HintState.Normal else if (nums2.size == size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        // 2. A number must appear once for every row.
        Stream.range(0, rows().toLong()).foreachDoEffect(Effect1<Int> { r: Int -> row2state[r] = f.f(Stream.range(0, cols().toLong()).map<Int>(F<Int, Int> { c: Int -> get(r, c) }).toJavaList()) })
        // 2. A number must appear once for every column.
        Stream.range(0, cols().toLong()).foreachDoEffect(Effect1<Int> { c: Int -> col2state[c] = f.f(Stream.range(0, rows().toLong()).map<Int>(F<Int, Int> { r: Int -> get(r, c) }).toJavaList()) })
        for ((p, h) in game.pos2hint.entries) {
            val g: F2<Int, Int, HintState> = label@ F2<Int, Int, HintState> { n1: Int, n2: Int ->
                if (n1 == 0 || n2 == 0) return@label HintState.Normal
                val n: Int = h.result
                when (h.op) {
                    '+' -> return@label if (n1 + n2 == n) HintState.Complete else HintState.Error
                    '-' -> return@label if (n1 - n2 == n || n2 - n1 == n) HintState.Complete else HintState.Error
                    '*' -> return@label if (n1 * n2 == n) HintState.Complete else HintState.Error
                    '/' -> return@label if (n1 / n2 * n2 == n * n2 || n2 / n1 * n1 == n * n1) HintState.Complete else HintState.Error
                    'O' -> return@label if (n1 % 2 == 1 && n2 % 2 == 1) HintState.Complete else HintState.Error
                    'E' -> return@label if (n1 % 2 == 0 && n2 % 2 == 0) HintState.Complete else HintState.Error
                }
                HintState.Normal
            }
            val nums: List<Int> = fj.data.Array.array<Position>(*MathraxGame.offset2).map<Int>(F<Position, Int> { os: Position? -> get(p.add(os)) }).toJavaList()
            // 3. This is valid for both pairs of numbers surrounding the hint.
            val s1: HintState = g.f(nums[0], nums[1])
            val s2: HintState = g.f(nums[2], nums[3])
            val s: HintState = if (s1 == HintState.Error || s2 == HintState.Error) HintState.Error else if (s1 == HintState.Complete && s2 == HintState.Complete) HintState.Complete else HintState.Normal
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    init {
        objArray = IntArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2state = arrayOfNulls<HintState>(rows())
        col2state = arrayOfNulls<HintState>(cols())
        updateIsSolved()
    }
}