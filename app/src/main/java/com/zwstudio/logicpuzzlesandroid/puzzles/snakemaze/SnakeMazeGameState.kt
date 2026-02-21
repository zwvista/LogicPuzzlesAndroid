package com.zwstudio.logicpuzzlesandroid.puzzles.snakemaze

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class SnakeMazeGameState(game: SnakeMazeGame) : CellsGameState<SnakeMazeGame, SnakeMazeGameMove, SnakeMazeGameState>(game) {
    var objArray = Array(rows * cols) { Array(4) { false } }
    var squares = mutableSetOf<Position>()
    var pos2stateHint = mutableMapOf<Position, HintState>()
    var pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: SnakeMazeGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        if (!isValid(p)) return GameOperationType.Invalid
        if (dir == SnakeMazeGame.PUZ_DIR_SQUARE) {
            if (!this[p].all { !it })
                return GameOperationType.Invalid
            if (!squares.remove(p))
                squares.add(p)
        } else {
            val (p2, dir2) = p + SnakeMazeGame.offset[dir] to (dir + 2) % 4
            if (!isValid(p2) || game.pos2hint.containsKey(p2) || game.pos2hint.containsKey(p) || game.pos2hint.containsKey(p2))
                return GameOperationType.Invalid
            this[p][dir] = !this[p][dir]
            this[p2][dir2] = !this[p2][dir2]
        }
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Snake Maze

        Summary
        Find the snakes using the given hints.

        Description
        1. A Snake is a path of five tiles, numbered 1-2-3-4-5, where 1 is the head and 5 the tail.
           The snake's body segments are connected horizontally or vertically.
        2. A snake cannot see another snake or it would attack it. A snake sees straight in the
           direction 2-1, that is to say it sees in front of the number 1.
        3. A snake cannot touch another snake horizontally or vertically.
        4. Arrows show you the closest piece of Snake in that direction (before another arrow or the edge).
        5. Arrows with zero mean that there is no Snake in that direction.
        6. Arrows block snake sight and also block other arrows hints.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 4. It is up to you to find the Squares, which are pointed at by the Arrows!
        // 5. The numbers beside the Arrows tell you how many Squares are present
        //    in that direction, from that point.
        for ((p, hint) in game.pos2hint) {
            val n2 = hint.num
            val os = SnakeMazeGame.offset[hint.dir]
            var n1 = 0
            var p2 = p + os
            while (isValid(p2)) {
                if (squares.contains(p2)) n1++
                p2 += os
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 6. The Squares can't touch horizontally or vertically.
        for (p in squares) {
            val s = if (!SnakeMazeGame.offset.any {
                squares.contains(p + it)
            }) AllowedObjectState.Normal else AllowedObjectState.Error
            pos2stateAllowed[p] = s
            if (s == AllowedObjectState.Error) isSolved = false
        }
        if (!isSolved) return
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                val dirs = (0 until 4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a loop that runs through all tiles.
                    pos2dirs[p] = dirs
                else if (!(dirs.isEmpty() && (game.pos2hint.containsKey(p) || squares.contains(p)))) {
                    // 2. The loop cannot cross itself.
                    isSolved = false; return
                }
            }
        // Check the loop
        val p = pos2dirs.keys.first()
        var p2 = p
        var n = -1
        while (true) {
            val dirs = pos2dirs[p2]
            if (dirs == null) { isSolved = false; return }
            pos2dirs.remove(p2)
            n = dirs.first { (it + 2) % 4 != n }
            p2 += SnakeMazeGame.offset[n]
            if (p2 == p) break
        }
    }
}