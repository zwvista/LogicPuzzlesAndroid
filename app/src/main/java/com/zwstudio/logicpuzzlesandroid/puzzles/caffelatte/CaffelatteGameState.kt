package com.zwstudio.logicpuzzlesandroid.puzzles.caffelatte

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame

class CaffelatteGameState(game: CaffelatteGame) : CellsGameState<CaffelatteGame, CaffelatteGameMove, CaffelatteGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CaffelatteGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Caffelatte

        Summary
        Cows and Coffee

        Description
        1. Make Cappuccino by linking each cup to one or more coffee beans and cows.
        2. Links must be straight lines, not crossing each other.
        3. To each cup there must be linked an equal number of beans and cows. At
           least one of each.
        4. When linking multiple beans and cows, you can also link cows to cows and
           beans to beans, other than linking them to the cup.
    */
    private fun updateIsSolved() {
        isSolved = true
        val coffeeList = mutableListOf<Position>()
        val sugarList = mutableListOf<Position>()
        val emptyList = mutableListOf<Position>()
        val ch2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = this[p]
                val ch = game[p]
                val dirs = (0 until 4).filter { o[it] }
                ch2dirs[p] = dirs
                val cnt = dirs.size
                when (ch) {
                    CaffelatteGame.PUZ_BEAN -> {
                        if (cnt != 1) { isSolved = false; return }
                        coffeeList.add(p)
                    }
                    CaffelatteGame.PUZ_CUP -> {
                        if (cnt != 1) { isSolved = false; return }
                        sugarList.add(p)
                    }
                    else -> {
                        if (!listOf(0, 2, 3).contains(cnt)) { isSolved = false; return }
                        if (cnt != 0) { emptyList.add(p) }
                    }
                }
            }
        val sugarList2 = mutableListOf<Position>()
        val emptyList2 = mutableListOf<Position>()
        for (p in coffeeList) {
            val i = ch2dirs[p]!![0]
            var os = CaffelatteGame.offset[i]
            var p2 = p + os
            var dirs: List<Int>
            while (true) {
                val ch = game[p2]
                if (ch != ' ') { isSolved = false; return }
                emptyList2.add(p2)
                val j = (i + 2) % 4
                dirs = ch2dirs[p2]!!
                if (!dirs.contains(j)) { isSolved = false; return }
                dirs = dirs.filter { it != j }
                if (dirs.size == 2) {
                    if (dirs.contains(i)) { isSolved = false; return }
                    break
                }
                val k = dirs[0]
                if (k != i) { isSolved = false; return }
                p2 += os
            }
            for (i in dirs) {
                os = CaffelatteGame.offset[i]
                var p3 = p2 + os
                while (true) {
                    val ch = game[p3]
                    if (!(ch == ' ' || ch == CaffelatteGame.PUZ_CUP)) { isSolved = false; return }
                    var dirs2 = ch2dirs[p3]!!
                    val j = (i + 2) % 4
                    if (!dirs2.contains(j)) { isSolved = false; return }
                    if (ch == CaffelatteGame.PUZ_CUP) {
                        sugarList2.add(p3)
                        break
                    }
                    emptyList2.add(p3)
                    dirs2 = dirs2.filter { it != j }
                    if (dirs2.size != 1) { isSolved = false; return }
                    val k = dirs2[0]
                    if (k != i) { isSolved = false; return }
                    p3 += os
                }
            }
        }
        if (sugarList.size to emptyList.size != sugarList2.size to emptyList2.size) { isSolved = false }
    }
}