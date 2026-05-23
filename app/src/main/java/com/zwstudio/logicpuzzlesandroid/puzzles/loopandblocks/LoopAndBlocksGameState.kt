package com.zwstudio.logicpuzzlesandroid.puzzles.loopandblocks

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.yalooniq.YalooniqGame

class LoopAndBlocksGameState(game: LoopAndBlocksGame) : CellsGameState<LoopAndBlocksGame, LoopAndBlocksGameMove, LoopAndBlocksGameState>(game) {
    val objArray = Array(rows * cols) { Array(4) { false } }
    val squares = mutableSetOf<Position>()
    val pos2stateHint = mutableMapOf<Position, HintState>()
    val pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, dotObj: Array<Boolean>) {objArray[row * cols + col] = dotObj}
    operator fun set(p: Position, obj: Array<Boolean>) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: LoopAndBlocksGameMove): GameOperationType {
        val (p, dir) = move.p to move.dir
        if (!isValid(p)) return GameOperationType.Invalid
        if (dir == LoopAndBlocksGame.PUZ_DIR_SQUARE) {
            if (!this[p].all { !it })
                return GameOperationType.Invalid
            if (!squares.remove(p))
                squares.add(p)
        } else {
            val (p2, dir2) = p + LoopAndBlocksGame.offset[dir] to (dir + 2) % 4
            if (!isValid(p2) || game.pos2hint.containsKey(p2) || game.pos2hint.containsKey(p) || game.pos2hint.containsKey(p2))
                return GameOperationType.Invalid
            this[p][dir] = !this[p][dir]
            this[p2][dir2] = !this[p2][dir2]
        }
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 7/Loop and Blocks

        Summary
        Don't block me now

        Description
        1. Draw a loop that passes through each clear tile.
        2. The loop must be a single one and can't intersect itself.
        3. A number in a cell shows how many cell must be shaded around its
           four sides.
        4. Not all cells that must be shaded are given with a hint. Two shaded
           cells can't touch orthogonally.
        5. The loop must pass over every cell that hasn't got a number or has
           not been shaded.
    */
    private fun updateIsSolved() {
        isSolved = true
        // 3. A number in a cell shows how many cell must be shaded around its
        //    four sides.
        // 4. Not all cells that must be shaded are given with a hint.
        for ((p, n2) in game.pos2hint) {
            val n1 = LoopAndBlocksGame.offset.count { squares.contains(p + it) }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        // 4. Two shaded cells can't touch orthogonally.
        for (p in squares) {
            val s = if (!YalooniqGame.offset.any {
                squares.contains(p + it)
            }) AllowedObjectState.Normal else AllowedObjectState.Error
            pos2stateAllowed[p] = s
            if (s == AllowedObjectState.Error) isSolved = false
        }
        if (!isSolved) return
        val pos2dirs = mutableMapOf<Position, List<Int>>()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                val dirs = (0..<4).filter { this[p][it] }
                if (dirs.size == 2)
                    // 1. Draw a loop that passes through each clear tile.
                    pos2dirs[p] = dirs
                else if (!(dirs.isEmpty() && (game.pos2hint.containsKey(p) || squares.contains(p)))) {
                    // 2. The loop must be a single one and can't intersect itself.
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
            p2 += YalooniqGame.offset[n]
            if (p2 == p) break
        }
    }
}