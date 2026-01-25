package com.zwstudio.logicpuzzlesandroid.puzzles.fingerpointing

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class FingerPointingGameState(game: FingerPointingGame) : CellsGameState<FingerPointingGame, FingerPointingGameMove, FingerPointingGameState>(game) {
    var objArray = Array<FingerPointingObject>(rows * cols) { FingerPointingEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: FingerPointingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: FingerPointingObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = FingerPointingHintObject()
        updateIsSolved()
    }

    override fun setObject(move: FingerPointingGameMove): GameOperationType {
        if (this[move.p] == move.obj) return GameOperationType.Invalid
        this[move.p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: FingerPointingGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is FingerPointingEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) FingerPointingMarkerObject else FingerPointingMineObject
            is FingerPointingMineObject -> if (markerOption == MarkerOptions.MarkerLast) FingerPointingMarkerObject else FingerPointingEmptyObject
            is FingerPointingMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) FingerPointingMineObject else FingerPointingEmptyObject
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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is FingerPointingForbiddenObject)
                    this[r, c] = FingerPointingEmptyObject
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in FingerPointingGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o is FingerPointingMineObject)
                    n1++
                else if (o is FingerPointingEmptyObject)
                    rng.add(+p2)
            }
            // 2. Numbers tell you how many mines there are close by, touching that
            // number horizontally, vertically or diagonally.
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = FingerPointingForbiddenObject
        }
    }
}