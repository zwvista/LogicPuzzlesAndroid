package com.zwstudio.logicpuzzlesandroid.puzzles.scissors

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class ScissorsGameState(game: ScissorsGame) : CellsGameState<ScissorsGame, ScissorsGameMove, ScissorsGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    val objArray = Array(rows * cols) { ScissorsObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: ScissorsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: ScissorsObject) {this[p.row, p.col] = obj}

    override fun setObject(move: ScissorsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: ScissorsGameMove): GameOperationType {
        val p = move.p
        move.obj = when (val o = this[p]) {
            ScissorsObject.Empty -> ScissorsObject.Forward
            ScissorsObject.Forward -> ScissorsObject.Backward
            ScissorsObject.Backward -> ScissorsObject.Empty
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 1/Scissors

        Summary
        Tailor's puzzle

        Description
        1. Cut the board into patches.
        2. Each patch should contain the numbers 1 to N exactly once (N being the highest number on the board).
        3. Each patch should end on the border.
    */
    private fun updateIsSolved() {
        isSolved = true
        val matrix = mutableMapOf<Position, MutableList<Position>>()
        val rng = mutableSetOf<Position>()
        // 1. Fill the board with diagonal lines (Slants), following the hints at
        //    the intersections.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                fun addSlash(p1: Position, p2: Position) {
                    matrix.getOrPut(p1) { mutableListOf() }.add(p2)
                    matrix.getOrPut(p2) { mutableListOf() }.add(p1)
                    rng.add(p1)
                    rng.add(p2)
                }
                when (this[p]) {
                    ScissorsObject.Forward -> addSlash(p, p + ScissorsGame.offset2[3])
                    ScissorsObject.Backward -> addSlash(p + ScissorsGame.offset2[1], p + ScissorsGame.offset2[2])
                    else -> isSolved = false
                }
            }
        // 2. Every number tells you how many Slants (diagonal lines) touch that
        //    point. So, for example, a 4 designates an X pattern around it.
        for ((p, n2) in game.pos2hint) {
            val n1 = matrix.getOrPut(p) { mutableListOf() }.size
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
        }
        if (!isSolved) return
        // 3. The Mazes or paths the Slants will form will usually branch off many
        //    times, but can also end abruptly. Also all the Slants don't need to
        //    be all connected.
        // 4. However, you must ensure that they don't form a closed loop anywhere.
        //    This also means very big loops, not just 2*2.
        while (rng.isNotEmpty()) {
            val moves = mutableSetOf<Position>()
            fun dfs(p: Position, pLast: Position): Boolean {
                if (!moves.add(p)) return false
                for (p2 in matrix[p]!!) {
                    if (p2 == pLast) continue
                    if (!dfs(p2, p)) return false
                }
                return true
            }
            if (!dfs(rng.first(), Position(-1, -1))) { isSolved = false; return }
            for (p in moves)
                rng.remove(p)
        }
    }
}
