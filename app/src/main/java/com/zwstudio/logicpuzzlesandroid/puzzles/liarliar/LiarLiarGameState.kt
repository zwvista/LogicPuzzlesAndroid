package com.zwstudio.logicpuzzlesandroid.puzzles.liarliar

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class LiarLiarGameState(game: LiarLiarGame) : CellsGameState<LiarLiarGame, LiarLiarGameMove, LiarLiarGameState>(game) {
    var objArray = Array<LiarLiarObject>(rows * cols) { LiarLiarEmptyObject }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: LiarLiarObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: LiarLiarObject) {this[p.row, p.col] = obj}

    init {
        for ((p, _) in game.pos2hint)
            this[p] = LiarLiarHintObject()
        updateIsSolved()
    }

    override fun setObject(move: LiarLiarGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint[p] != null || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: LiarLiarGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || game.pos2hint[p] != null) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is LiarLiarEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) LiarLiarMarkerObject else LiarLiarMarkedObject
            is LiarLiarMarkedObject -> if (markerOption == MarkerOptions.MarkerLast) LiarLiarMarkerObject else LiarLiarEmptyObject
            is LiarLiarMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) LiarLiarMarkedObject else LiarLiarEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 2/Liar Liar

        Summary
        Tiles on fire

        Description
        1. Mark some tiles according to these rules:
        2. Cells with numbers are never marked.
        3. A number in a cell indicates how many marked cells must be placed.
           adjacent to its four sides.
        4. However, in each region there is one (and only one) wrong number
           (it shows a wrong amount of marked cells).
        5. Two marked cells must not be orthogonally adjacent.
        6. All of the non-marked cells must be connected.
    */
    private fun updateIsSolved() {
        isSolved = true
    }
}