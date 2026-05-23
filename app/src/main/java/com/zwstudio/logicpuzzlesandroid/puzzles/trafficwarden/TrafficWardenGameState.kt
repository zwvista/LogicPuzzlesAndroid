package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwarden

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrafficWardenGameState(game: TrafficWardenGame) : CellsGameState<TrafficWardenGame, TrafficWardenGameMove, TrafficWardenGameState>(game) {
    val objArray = Array(rows * cols) { Array(4) { false } }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TrafficWardenGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + TrafficWardenGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 2/Traffic Warden

        Summary
        But the light was green!

        Description
        1. Draw a single non intersecting, continuous looping path which must
           follow these rules at every traffic light:
        2. While passing on green lights, the road must go straight, it can't turn.
        3. While passing on red lights, the road must turn 90 degrees.
        4. While passing on yellow lights, the road might turn 90 degrees or
           go straight.
        5. A number on the light tells you the length of the straights coming
           out of that tile.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                pos2dirs[p] = dirs
                // 1. Draw a single non intersecting, continuous looping path
                if (!(dirs.size == 2 || dirs.isEmpty() && game.pos2hint[p] == null)) isSolved = false
            }
        for ((p, hint) in game.pos2hint) {
            val ch = hint.light
            val dirs = pos2dirs[p]!!
            // 2. While passing on green lights, the road must go straight, it can't turn.
            // 3. While passing on red lights, the road must turn 90 degrees.
            // 4. While passing on yellow lights, the road might turn 90 degrees or
            //    go straight.
            if (!(dirs.size == 2 && (
                (ch == TrafficWardenGame.PUZ_GREEN || ch == TrafficWardenGame.PUZ_YELLOW) && dirs[1] - dirs[0] == 2 ||
                (ch == TrafficWardenGame.PUZ_RED || ch == TrafficWardenGame.PUZ_YELLOW) && dirs[1] - dirs[0] != 2))
            ) {
                isSolved = false; pos2state[p] = HintState.Normal
            } else {
                // 5. A number on the light tells you the length of the straights coming
                //    out of that tile.
                val n2 = hint.len
                var n1 = 0
                for (d in dirs) {
                    val os = TrafficWardenGame.offset[d]
                    var p2 = p + os
                    n1++
                    while (true) {
                        val dirs2 = pos2dirs[p2]!!
                        if (!dirs2.contains(d)) break
                        p2 += os
                        n1++
                    }
                }
                val s = if (n1 == n2) HintState.Complete else HintState.Error
                if (s != HintState.Complete) isSolved = false
                pos2state[p] = s
            }
        }
        if (!isSolved) return
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += TrafficWardenGame.offset[n]
            if (p2 == p) break
        }
    }
}