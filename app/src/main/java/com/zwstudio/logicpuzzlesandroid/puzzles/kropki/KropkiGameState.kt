package com.zwstudio.logicpuzzlesandroid.puzzles.kropki

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class KropkiGameState(game: KropkiGame) : CellsGameState<KropkiGame, KropkiGameMove, KropkiGameState>(game) {
    var objArray = IntArray(rows * cols)
    var pos2horzHint = mutableMapOf<Position, HintState>()
    var pos2vertHint = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: Int) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: Int) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: KropkiGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    override fun switchObject(move: KropkiGameMove): Boolean {
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = (o + 1) % (game.cols + 1)
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Kropki

        Summary
        Fill the rows and columns with numbers, respecting the relations

        Description
        1. The Goal is to enter numbers 1 to board size once in every row and
           column.
        2. A Dot between two tiles give you hints about the two numbers:
        3. Black Dot - one number is twice the other.
        4. White Dot - the numbers are consecutive.
        5. Where the numbers are 1 and 2, there can be either a Black Dot(2 is
           1*2) or a White Dot(1 and 2 are consecutive).
        6. Please note that when two numbers are either consecutive or doubles,
           there MUST be a Dot between them!

        Variant
        7. In later 9*9 levels you will also have bordered and coloured areas,
           which must also contain all the numbers 1 to 9.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 1. The Goal is to enter numbers 1 to board size once in every row.
        for (r in 0 until rows) {
            val nums = (0 until cols).map { this[r, it] }.toSet()
            if (nums.contains(0) || nums.size != cols) isSolved = false
        }
        // 1. The Goal is to enter numbers 1 to board size once in every column.
        for (c in 0 until cols) {
            val nums = (0 until rows).map { this[it, c] }.toSet()
            if (nums.contains(0) || nums.size != rows) isSolved = false
        }
        // 7. In later 9*9 levels you will also have bordered and coloured areas,
        // which must also contain all the numbers 1 to 9.
        if (game.bordered)
            for (a in game.areas) {
                val nums = a.map { this[it] }.toSet()
                if (nums.contains(0) || nums.size != a.size) isSolved = false
            }
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                for (i in 0..1) {
                    if (i == 0 && c == cols - 1 || i == 1 && r == rows - 1) continue
                    var n1 = this[p]
                    var n2 = this[r + i, c + 1 - i]
                    if (n1 == 0 || n2 == 0) {
                        (if (i == 0) pos2horzHint else pos2vertHint)[p] = HintState.Normal
                        isSolved = false
                        continue
                    }
                    if (n1 > n2) {
                        val temp = n1
                        n1 = n2
                        n2 = temp
                    }
                    val kh = (if (i == 0) game.pos2horzHint else game.pos2vertHint)[p]
                    // 3. Black Dot - one number is twice the other.
                    // 4. White Dot - the numbers are consecutive.
                    // 5. Where the numbers are 1 and 2, there can be either a Black Dot(2 is
                    // 1*2) or a White Dot(1 and 2 are consecutive).
                    // 6. Please note that when two numbers are either consecutive or doubles,
                    // there MUST be a Dot between them!
                    val s = if (n2 != n1 + 1 && n2 != n1 * 2 && kh == KropkiHint.None || n2 == n1 + 1 && kh == KropkiHint.Consecutive || n2 == n1 * 2 && kh == KropkiHint.Twice) HintState.Complete else HintState.Error
                    (if (i == 0) pos2horzHint else pos2vertHint)[p] = s
                    if (s != HintState.Complete) isSolved = false
                }
            }
    }
}