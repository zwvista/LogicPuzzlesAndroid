package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FingerPointingGameState(game: FingerPointingGame) : CellsGameState<FingerPointingGame, FingerPointingGameMove, FingerPointingGameState>(game) {
    var objArray = game.objArray.copyOf()
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FingerPointingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FingerPointingObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: FingerPointingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FingerPointingObject.Empty || this[p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FingerPointingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game[p] != FingerPointingObject.Empty) return GameOperationType.Invalid
        move.obj = when (val o = this[p]) {
            FingerPointingObject.Empty -> FingerPointingObject.Up
            FingerPointingObject.Up -> FingerPointingObject.Right
            FingerPointingObject.Right -> FingerPointingObject.Down
            FingerPointingObject.Down -> FingerPointingObject.Left
            FingerPointingObject.Left -> FingerPointingObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 5/Finger Pointing

        Summary
        Blame is in the air

        Description
        1. Fill the board with fingers. Two fingers pointing in the same direction
           can't be orthogonally adjacent.
        2. the number tells you how many fingers and finger 'trails' point to that tile.
    */
    private fun updateIsSolved() {
        isSolved = true
        val pos2rng = mutableMapOf<Position, MutableSet<Position>>()
        pos2state.clear()
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                var o = this[p]
                if (o == FingerPointingObject.Empty) { isSolved = false; continue }
                if (o == FingerPointingObject.Block || o == FingerPointingObject.Hint) continue
                pos2state[p] = pos2state[p] ?: HintState.Normal
                // 1. Fill the board with fingers. Two fingers pointing in the same direction
                //    can't be orthogonally adjacent.
                val rng = FingerPointingGame.offset.map { p + it }.filter { isValid(it) && this[it] == o }
                if (rng.isNotEmpty()) {
                    isSolved = false
                    for (p2 in rng) pos2state[p2] = HintState.Error
                }
                var p2 = p
                val rng2 = mutableListOf<Position>()
                while (true) {
                    rng2.add(p2)
                    p2 += FingerPointingGame.offset[o.ordinal - FingerPointingObject.Up.ordinal]
                    if (!isValid(p2) || rng2.contains(p2)) break
                    o = this[p2]
                    if (o == FingerPointingObject.Empty) { isSolved = false; break }
                    if (o == FingerPointingObject.Block) break
                    if (o == FingerPointingObject.Hint) {
                        pos2rng.getOrPut(p2) { mutableSetOf() }.addAll(rng2)
                        break
                    }
                }
            }
        // 2. the number tells you how many fingers and finger 'trails' point to that tile.
        for ((p, n2) in game.pos2hint) {
            val n1 = pos2rng[p]?.size ?: 0
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (s != HintState.Complete) isSolved = false
            pos2state[p] = s
        }
    }
}