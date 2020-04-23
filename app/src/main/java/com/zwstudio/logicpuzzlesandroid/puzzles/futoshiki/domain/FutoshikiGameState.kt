package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F
import fj.F0
import fj.Ord
import fj.data.Set
import fj.data.Stream
import java.util.*

class FutoshikiGameState(game: FutoshikiGame) : CellsGameState<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>(game) {
    var objArray: CharArray
    var row2state: Array<HintState?>
    var col2state: Array<HintState?>
    var pos2state = mutableMapOf<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: Char) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: Char) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: FutoshikiGameMove): Boolean {
        val p = move!!.p
        if (!(isValid(p) && p!!.row % 2 == 0 && p.col % 2 == 0 && game!![p] == ' ') || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: FutoshikiGameMove): Boolean {
        val p = move!!.p
        if (!(isValid(p) && p!!.row % 2 == 0 && p.col % 2 == 0 && game!![p] == ' ')) return false
        val o = get(p).toInt()
        move.obj = if (o == ' '.toInt()) '1' else if (o == '1'.toInt() + rows() / 2) ' ' else (o + 1).toChar()
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 2/Futoshiki

        Summary
        Fill the rows and columns with numbers, respecting the relations

        Description
        1. In a manner similar to Sudoku, you have to put in each row and column
           numbers ranging from 1 to N, where N is the puzzle board size.
        2. The hints you have are the 'less than'/'greater than' signs between tiles.
        3. Remember you can't repeat the same number in a row or column.

        Variation
        4. Some boards, instead of having less/greater signs, have just a line
           separating the tiles.
        5. That separator hints at two tiles with consecutive numbers, i.e. 1-2
           or 3-4..
        6. Please note that in this variation consecutive numbers MUST have a
           line separating the tiles. Otherwise they're not consecutive.
        7. This Variation is a taste of a similar game: 'Consecutives'.
    */
    private fun updateIsSolved() {
        isSolved = true
        val f = F { nums: List<Char> ->
            val nums2 = Set.iterableSet(Ord.charOrd, nums).toJavaList()
            // 1. You have to put in each row and column
            // numbers ranging from 1 to N, where N is the puzzle board size.
            val s = if (nums2[0] == ' ') HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            s
        }
        // 3. Remember you can't repeat the same number in a row.
        range(0, rows(), 2).foreachDoEffect { r: Int -> row2state[r] = f.f(range(0, cols(), 2).map { c: Int -> get(r, c) }.toJavaList()) }
        // 3. Remember you can't repeat the same number in a column.
        range(0, cols(), 2).foreachDoEffect { c: Int -> col2state[c] = f.f(range(0, rows(), 2).map { r: Int -> get(r, c) }.toJavaList()) }
        for ((p, h) in game!!.pos2hint) {
            val r = p.row
            val c = p.col
            val ch1 = if (r % 2 == 0) get(r, c - 1) else get(r - 1, c)
            val ch2 = if (r % 2 == 0) get(r, c + 1) else get(r + 1, c)
            val g = label@ F0<HintState> {
                if (ch1 == ' ' || ch2 == ' ') return@label HintState.Normal
                val n1 = ch1 - '0'
                val n2 = ch2 - '0'
                when (h) {
                    '^', '<' -> return@label if (n1 < n2) HintState.Complete else HintState.Error
                    'v', '>' -> return@label if (n1 > n2) HintState.Complete else HintState.Error
                    else -> return@label HintState.Normal
                }
            }
            val s = g.f()
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }

    companion object {
        private fun range(from: Int, to: Int, step: Int) = if (from >= to) Stream.nil() else Stream.cons(from, { range(from + step, to, step) })
    }

    init {
        objArray = CharArray(rows() * cols())
        System.arraycopy(game.objArray, 0, objArray, 0, objArray.size)
        row2state = arrayOfNulls(rows())
        col2state = arrayOfNulls(cols())
        updateIsSolved()
    }
}