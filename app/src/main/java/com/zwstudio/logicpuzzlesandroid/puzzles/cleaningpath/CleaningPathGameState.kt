package com.zwstudio.logicpuzzlesandroid.puzzles.cleaningpath

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class CleaningPathGameState(game: CleaningPathGame) : CellsGameState<CleaningPathGame, CleaningPathGameMove, CleaningPathGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: CleaningPathGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        val (p2, dir2) = p + CleaningPathGame.offset[dir] to (dir + 2) % 4
        if (!isValid(p2))
            return GameOperationType.Invalid
        this[p][dir] = !this[p][dir]
        this[p2][dir2] = !this[p2][dir2]
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Cleaning Path

        Summary
        Puzzles for Roombas

        Description
        1. You are a Roomba! And this is office floor you have to clean tonight.
        2. The floor is divided in rooms. You can enter (and exit) the room only once.
        3. Follow a path that allows you to clean all the tiles on the floor.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 3. Follow a path that allows you to clean all the tiles on the floor.
                    pos2dirs[p] = dirs
                else {
                    // The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.firstOrNull()
        if (p == null) { isSolved = false; return }
        var p2 = p
        var n = -1
        var lastArea = -1
        val area2count = mutableMapOf<Int, Int>()
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            val area = game.pos2area[p2]!!
            if (area != lastArea) {
                area2count[area] = (area2count[area] ?: 0) + 1
                lastArea = area
            }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += CleaningPathGame.offset[n]
            if (p2 == p) {
                if (area == game.pos2area[p]!!)
                    area2count[area] = area2count[area]!! - 1
                break
            }
        }
        // 1. You are a Roomba! And this is office floor you have to clean tonight.
        // 2. The floor is divided in rooms. You can enter (and exit) the room only once.
        if (!(area2count.size == game.areas.size && area2count.all { it.value == 1 })) isSolved = false
    }
}