package com.zwstudio.logicpuzzlesandroid.puzzles.youturnmeon

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class YouTurnMeOnGameState(game: YouTurnMeOnGame) : CellsGameState<YouTurnMeOnGame, YouTurnMeOnGameMove, YouTurnMeOnGameState>(game) {
    val objArray = Array(rows * cols) { Array(4) { false } }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: YouTurnMeOnGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + YouTurnMeOnGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/You Turn me on

        Summary
        Sometimes you do, sometimes you don't

        Description
        1. Draw a single, no intersecting loop.
        2. The number on each region tells you how many turns the path does
           in that region.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                pos2dirs[p] = dirs
                if (dirs.size != 2)
                    // 1. Draw a single, no intersecting loop.
                    isSolved = false
            }
        // 2. The number on each region tells you how many turns the path does
        //    in that region.
        for ((p, n2) in game.pos2hint) {
            val area = game.areas[game.pos2area[p]!!]
            val n1 = area.fold(0) { acc, p ->
                val dirs = pos2dirs[p]!!
                acc + (if (dirs.size == 2 && dirs[1] - dirs[0] != 2) 1 else 0)
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[p] = s
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
            p2 += YouTurnMeOnGame.offset[n]
            if (p2 == p) break
        }
    }
}