package com.zwstudio.logicpuzzlesandroid.puzzles.coffeeandsugar

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame

class CoffeeAndSugarGameState(game: CoffeeAndSugarGame) : CellsGameState<CoffeeAndSugarGame, CoffeeAndSugarGameMove, CoffeeAndSugarGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CoffeeAndSugarGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/CoffeeAndSugar

        Summary
        Draw a Necklace that goes through every Pearl

        Description
        1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
           that never branches-off or crosses itthis.
        2. The rules to pass Pearls are:
        3. Lines passing through White Pearls must go straight through them.
           However, at least at one side of the White Pearl(or both), they must
           do a 90 degree turn.
        4. Lines passing through Black Pearls must do a 90 degree turn in them.
           Then they must go straight in the next tile in both directions.
        5. Lines passing where there are no Pearls can do what they want.
    */
    private fun updateIsSolved() {
        isSolved = true
        val coffeeList = mutableListOf<Position>()
        val sugarList = mutableListOf<Position>()
        val emptyList = mutableListOf<Position>()
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val o = this[p]
                val ch = game[p]
                val dirs = (0..<4).filter { o[it] }
                pos2dirs[p] = dirs
                val cnt = dirs.size
                when (ch) {
                    CoffeeAndSugarGame.PUZ_COFFEE -> {
                        if (cnt != 1) { isSolved = false; return }
                        coffeeList.add(p)
                    }
                    CoffeeAndSugarGame.PUZ_SUGAR -> {
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
            val i = pos2dirs[p]!![0]
            var os = CoffeeAndSugarGame.offset[i]
            var p2 = p + os
            var dirs: List<Int>
            while (true) {
                val ch = game[p2]
                if (ch != ' ') { isSolved = false; return }
                emptyList2.add(p2)
                val j = (i + 2) % 4
                dirs = pos2dirs[p2]!!
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
                os = CoffeeAndSugarGame.offset[i]
                var p3 = p2 + os
                while (true) {
                    val ch = game[p3]
                    if (!(ch == ' ' || ch == CoffeeAndSugarGame.PUZ_SUGAR)) { isSolved = false; return }
                    var dirs2 = pos2dirs[p3]!!
                    val j = (i + 2) % 4
                    if (!dirs2.contains(j)) { isSolved = false; return }
                    if (ch == CoffeeAndSugarGame.PUZ_SUGAR) {
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