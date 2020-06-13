package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class FutoshikiGameState(game: FutoshikiGame) : CellsGameState<FutoshikiGame, FutoshikiGameMove, FutoshikiGameState>(game) {
    var objArray = game.objArray.copyOf()
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Char) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Char) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: FutoshikiGameMove): Boolean {
        val p = move.p
        if (!(isValid(p) && p.row % 2 == 0 && p.col % 2 == 0 && game[p] == ' ') || this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: FutoshikiGameMove): Boolean {
        val p = move.p
        if (!(isValid(p) && p.row % 2 == 0 && p.col % 2 == 0 && game[p] == ' ')) return false
        val o = this[p].toInt()
        move.obj = if (o == ' '.toInt()) '1' else if (o == '1'.toInt() + rows / 2) ' ' else (o + 1).toChar()
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
        fun f(nums: List<Char>): HintState {
            val nums2 = nums.toSet()
            // 1. You have to put in each row and column
            // numbers ranging from 1 to N, where N is the puzzle board size.
            val s = if (nums2.first() == ' ') HintState.Normal else if (nums2.size == nums.size) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            return s
        }
        // 3. Remember you can't repeat the same number in a row.
        for (r in 0..rows step 2)
            row2state[r] = f((0..cols step 2).map { this[r, it] } )
        // 3. Remember you can't repeat the same number in a column.
        for (c in 0..cols step 2)
            col2state[c] = f((0..rows step 2).map { this[it, c] } )
        for ((p, h) in game.pos2hint) {
            val r = p.row
            val c = p.col
            val ch1 = if (r % 2 == 0) this[r, c - 1] else this[r - 1, c]
            val ch2 = if (r % 2 == 0) this[r, c + 1] else this[r + 1, c]
            fun g(): HintState {
                if (ch1 == ' ' || ch2 == ' ') return HintState.Normal
                val n1 = ch1 - '0'
                val n2 = ch2 - '0'
                when (h) {
                    '^', '<' -> return if (n1 < n2) HintState.Complete else HintState.Error
                    'v', '>' -> return if (n1 > n2) HintState.Complete else HintState.Error
                    else -> return HintState.Normal
                }
            }
            val s = g()
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}