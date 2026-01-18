package com.zwstudio.logicpuzzlesandroid.puzzles.turnmeup

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.MasyuGame

class TurnMeUpGameState(game: TurnMeUpGame) : CellsGameState<TurnMeUpGame, TurnMeUpGameMove, TurnMeUpGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TurnMeUpGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 2/Turn me up

        Summary
        How many turns

        Description
        1. Connect the circles between them, in pairs.
        2. The number on the circle tells you how many turns the connection
           does between circles.
        3. Two circles without numbers can have any number of turns.
        4. All tiles on the board must be used and all circles must be connected.
    */
    private fun updateIsSolved() {
        isSolved = true
        val circles = mutableSetOf<Position>()
        val ch2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val o = get(r, c)
                val ch = game[r, c]
                val dirs = (0 until 4).filter { o[it] }
                // 2. The number on the circle tells you how many turns the connection
                //    does between circles.
                when (dirs.size) {
                    1 -> {
                        if (ch == ' ') { isSolved = false; return }
                        circles.add(p)
                        ch2dirs[p] = dirs
                    }
                    2 -> {
                        if (ch != ' ') { isSolved = false; return }
                        ch2dirs[p] = dirs
                    }
                    else -> {
                        // 4. All tiles on the board must be used
                        isSolved = false; return
                    }
                }
            }
        // 2. You should draw as many lines into the grid as number sets:
        //    a line starts with the number 1, goes through the numbers in
        //    order up to the highest, where it ends.
        while (circles.isNotEmpty()) {
            val p = circles.first()
            val ch1 = game[p]
            var i = ch2dirs[p]!![0]
            var os = TurnMeUpGame.offset[i]
            var p2 = p + os
            var turns = 0
            while (true) {
                val j = (i + 2) % 4
                var dirs = ch2dirs[p2]!!
                dirs = dirs.filter { it != j }
                if (dirs.isEmpty()) break
                val k = dirs[0]
                if (k != i) {
                    turns++
                    i = k
                }
                os = TurnMeUpGame.offset[i]
                p2 += os
            }
            val ch2 = game[p2]
            if (ch1 == TurnMeUpGame.PUZ_QM || ch2 == TurnMeUpGame.PUZ_QM || ch1 == ch2 && ch1 - '0' == turns) {
              circles.remove(p); circles.remove(p2)
            } else {
                isSolved = false; return
            }
        }
    }
}