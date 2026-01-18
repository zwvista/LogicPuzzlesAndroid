package com.zwstudio.logicpuzzlesandroid.puzzles.rabbits

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class RabbitsGameState(game: RabbitsGame) : CellsGameState<RabbitsGame, RabbitsGameMove, RabbitsGameState>(game) {
    var objArray = Array<RabbitsObject>(rows * cols) { RabbitsEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: RabbitsObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: RabbitsObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = RabbitsHintObject()
        updateIsSolved()
    }

    override fun setObject(move: RabbitsGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: RabbitsGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is RabbitsEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsMarkerObject else RabbitsRabbitObject()
            is RabbitsRabbitObject -> RabbitsTreeObject()
            is RabbitsTreeObject -> if (markerOption == MarkerOptions.MarkerLast) RabbitsMarkerObject else RabbitsEmptyObject
            is RabbitsMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) RabbitsRabbitObject() else RabbitsEmptyObject
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
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                when (this[p]) {
                    is RabbitsRabbitObject -> this[p] = RabbitsRabbitObject()
                    is RabbitsTreeObject -> this[p] = RabbitsTreeObject()
                    is RabbitsForbiddenObject -> this[p] = RabbitsEmptyObject
                    else -> {}
                }
            }
        // 4. Each row and column has exactly one Tree and one Rabbit.
        fun f(rng: List<Position>) {
            val rngRabbit = rng.filter { this[it] is RabbitsRabbitObject }
            if (rngRabbit.size != 1) {
                isSolved = false
                for (p in rngRabbit) this[p] = RabbitsRabbitObject(state = AllowedObjectState.Error)
            }
            val rngTree = rng.filter { this[it] is RabbitsTreeObject }
            if (rngTree.size != 1) {
                isSolved = false
                for (p in rngTree) this[p] = RabbitsTreeObject(state = AllowedObjectState.Error)
            }
            if (allowedObjectsOnly && rngRabbit.size >= 1 && rngTree.size >= 1) {
                val rngEmpty = rng.filter { this[it] is RabbitsEmptyObject }
                for (p in rngEmpty) this[p] = RabbitsForbiddenObject
            }
        }
        for (r in 0 until rows) {
            val rng = (0 until cols).map { Position(r, it) }
            f(rng)
        }
        for (c in 0 until cols) {
            val rng = (0 until rows).map { Position(it, c) }
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
                        is RabbitsRabbitObject -> { n1 += 1; continue@next }
                        is RabbitsTreeObject -> continue@next
                        else -> {}
                    }
                    p2 += os
                }
            }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            this[p] = RabbitsHintObject(state = s)
            if (s != HintState.Complete) isSolved = false
        }
    }
}