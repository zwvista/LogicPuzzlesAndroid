package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RabbitsGameState(game: RabbitsGame) : CellsGameState<RabbitsGame, RabbitsGameMove, RabbitsGameState>(game) {
    val objArray = Array(rows * cols) { RabbitsObject.Empty }
    val pos2stateHint = mutableMapOf<Position, HintState>()
    val pos2stateAllowed = mutableMapOf<Position, AllowedObjectState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: RabbitsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: RabbitsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = RabbitsObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: RabbitsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == RabbitsObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RabbitsGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == RabbitsObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            RabbitsObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsObject.Marker else RabbitsObject.Rabbit
            RabbitsObject.Rabbit -> RabbitsObject.Tree
            RabbitsObject.Tree -> if (markerOption == MarkerOptions.MarkerLast) RabbitsObject.Marker else RabbitsObject.Empty
            RabbitsObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsObject.Rabbit else RabbitsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 3/Puzzle Set 3/Rabbits

        Summary
        Rabbit 'n' Seek

        Description
        1. The board represents a lawn where Rabbits are playing Hide 'n' Seek,
           behind Trees.
        2. Each number tells you how many Rabbits can be seen from that tile,
           in an horizontal and vertical line.
        3. Tree hide Rabbits, numbers don't.
        4. Each row and column has exactly one Tree and one Rabbit.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                when (this[p]) {
                    RabbitsObject.Rabbit, RabbitsObject.Tree -> pos2stateAllowed[p] = AllowedObjectState.Normal
                    RabbitsObject.Forbidden -> this[p] = RabbitsObject.Empty
                    else -> {}
                }
            }
        // 4. Each row and column has exactly one Tree and one Rabbit.
        fun f(rng: List<Position>) {
            val rngRabbit = rng.filter { this[it] == RabbitsObject.Rabbit }
            if (rngRabbit.size != 1) {
                isSolved = false
                for (p in rngRabbit) pos2stateAllowed[p] = AllowedObjectState.Error
            }
            val rngTree = rng.filter { this[it] == RabbitsObject.Tree }
            if (rngTree.size != 1) {
                isSolved = false
                for (p in rngTree) pos2stateAllowed[p] = AllowedObjectState.Error
            }
            if (allowedObjectsOnly && rngRabbit.isNotEmpty() && rngTree.isNotEmpty()) {
                val rngEmpty = rng.filter { this[it] == RabbitsObject.Empty }
                for (p in rngEmpty) this[p] = RabbitsObject.Forbidden
            }
        }
        for (r in 0..<rows) {
            val rng = (0..<cols).map { Position(r, it) }
            f(rng)
        }
        for (c in 0..<cols) {
            val rng = (0..<rows).map { Position(it, c) }
            f(rng)
        }
        // 2. Each number tells you how many Rabbits can be seen from that tile,
        //    in an horizontal and vertical line.
        // 3. Tree hide Rabbits, numbers don't.
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            next@ for (os in RabbitsGame.offset) {
                var p2 = p + os
                while (isValid(p2)) {
                    when (this[p2]) {
                        RabbitsObject.Rabbit -> { n1++; continue@next }
                        RabbitsObject.Tree -> continue@next
                        else -> {}
                    }
                    p2 += os
                }
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2stateHint[p] = s
            if (s != HintState.Complete) isSolved = false
        }
    }
}