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
           that never branches-off or crosses itself.
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
        val chOneList = mutableListOf<Position>()
        val ch2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = get(r, c)
                val ch = game[r, c]
                val dirs = (0 until 4).filter { o[it] }
                when (dirs.size) {
                    1 -> {
                        // 2. You should draw as many lines into the grid as number sets:
                        //    a line starts with the number 1, goes through the numbers in
                        //    order up to the highest, where it ends.
                        if (!(ch == CoffeeAndSugarGame.PUZ_ONE || ch == game.chMax)) { isSolved = false; return }
                        if (ch == CoffeeAndSugarGame.PUZ_ONE) chOneList.add(p)
                        ch2dirs[p] = dirs
                    }
                    2 -> ch2dirs[p] = dirs
                    else -> {
                        // 3. In doing this, you have to pass through all tiles on the board.
                        //    Lines cannot cross.
                        isSolved = false; return
                    }
                }
            }
        // 2. You should draw as many lines into the grid as number sets:
        //    a line starts with the number 1, goes through the numbers in
        //    order up to the highest, where it ends.
        for (p in chOneList) {
            var chars = CoffeeAndSugarGame.PUZ_ONE.toString()
            var i = ch2dirs[p]!![0]
            var os = CoffeeAndSugarGame.offset[i]
            var p2 = p + os
            while (true) {
                val ch = game[p2]
                if (ch != ' ') chars += ch
                val dirs = ch2dirs[p2]!!.filter { it != (i + 2) % 4 }
                if (dirs.isEmpty()) break
                i = dirs[0]
                os = CoffeeAndSugarGame.offset[i]
                p2 += os
            }
            if (chars != game.expectedChars) { isSolved = false; return }
        }
    }
}