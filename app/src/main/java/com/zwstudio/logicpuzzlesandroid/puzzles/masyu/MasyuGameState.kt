package com.zwstudio.logicpuzzlesandroid.puzzles.masyu

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.runinaloop.RunInALoopGame

class MasyuGameState(game: MasyuGame) : CellsGameState<MasyuGame, MasyuGameMove, MasyuGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: MasyuGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + MasyuGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: Logic Games/Puzzle Set 3/Masyu

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
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val ch = game[p]
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2) {
                    pos2dirs[p] = dirs
                    if (ch == MasyuGame.PUZ_BLACK_PEARL) {
                        // 4. Lines passing through Black Pearls must do a 90 degree turn in them.
                        if (dirs[1] - dirs[0] == 2) { isSolved = false; return }
                    } else if (ch == MasyuGame.PUZ_WHITE_PEARL) {
                        // 3. Lines passing through White Pearls must go straight through them.
                        if (dirs[1] - dirs[0] != 2) { isSolved = false; return }
                    }
                } else if (!(dirs.isEmpty() && ch == ' ')) {
                    // 1. The goal is to draw a single Loop(Necklace) through every circle(Pearl)
                    //    that never branches-off or crosses itself.
                    isSolved = false; return
                }
            }
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += RunInALoopGame.offset[n]
            if (p2 == p) break
        }
        // 3. At least at one side of the White Pearl(or both), they must do a 90 degree turn.
        // 4. Lines passing through Black Pearls must go straight in the next tile in both directions.
        // 5. Lines passing where there are no Pearls can do what they want.
        if (!pos2dirs.all { (p, dirs) ->
            when (val ch = game[p]) {
                ' ' -> true
                else -> {
                    val turns = dirs.reduce { acc, d ->
                        val dirs2 = pos2dirs[p + MasyuGame.offset[d]]!!
                        acc + (if (dirs2[1] - dirs2[0] != 2) 1 else 0)
                    }
                    ch == MasyuGame.PUZ_BLACK_PEARL && turns == 0 || ch == MasyuGame.PUZ_WHITE_PEARL && turns > 0
                }
            }
        }) isSolved = false
    }
}