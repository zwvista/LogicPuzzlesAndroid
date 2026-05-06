package com.zwstudio.logicpuzzlesandroid.puzzles.trafficwardenrevenge

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class TrafficWardenRevengeGameState(game: TrafficWardenRevengeGame) : CellsGameState<TrafficWardenRevengeGame, TrafficWardenRevengeGameMove, TrafficWardenRevengeGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: TrafficWardenRevengeGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + TrafficWardenRevengeGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2)) return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/Traffic Warden Revenge

        Summary
        But the ...oh well

        Description
        1. Draw a single non intersecting loop passing through all traffic lights.
        2. Green light means the road that extends from there is of equal length
           in both directions.
        3. Red light means they are not.
        4. A number tells you the sum of the length or road extending from that
           traffic light, be it green or red.
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
            if (dirs.size != 2) {
                isSolved = false; pos2state[p] = HintState.Normal
            } else {
                // 4. A number tells you the sum of the length or road extending from that
                //    traffic light, be it green or red.
                val n2 = hint.len
                val ns = IntArray(2) { 1 }
                for (i in 0..<2) {
                    val d = dirs[i]
                    val os = TrafficWardenRevengeGame.offset[d]
                    var p2 = p + os
                    while (true) {
                        val dirs2 = pos2dirs[p2]!!
                        if (!dirs2.contains(d)) break
                        p2 += os
                        ns[i]++
                    }
                }
                // 2. Green light means the road that extends from there is of equal length
                //    in both directions.
                // 3. Red light means they are not.
                val s =
                    if ((ns[0] == ns[1]) != (ch == TrafficWardenRevengeGame.PUZ_GREEN)) HintState.Normal
                    else if (n2 == TrafficWardenRevengeGame.PUZ_UNKNOWN ||
                        n2 == TrafficWardenRevengeGame.PUZ_UNKNOWN_10 && ns[0] + ns[1] >= 10 ||
                        ns[0] + ns[1] == n2) HintState.Complete
                    else HintState.Error
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
            p2 += TrafficWardenRevengeGame.offset[n]
            if (p2 == p) break
        }
    }
}