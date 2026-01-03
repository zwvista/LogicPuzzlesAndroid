package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PouringWaterGameState(game: PouringWaterGame) : CellsGameState<PouringWaterGame, PouringWaterGameMove, PouringWaterGameState>(game) {
    var objArray = Array<PouringWaterObject>(rows * cols) { PouringWaterEmptyObject }
    var row2state = Array(rows) { HintState.Normal }
    var col2state = Array(cols) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: PouringWaterObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: PouringWaterObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    override fun setObject(move: PouringWaterGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: PouringWaterGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            is PouringWaterEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) PouringWaterMarkerObject else PouringWaterWaterObject()
            is PouringWaterWaterObject -> if (markerOption == MarkerOptions.MarkerLast) PouringWaterMarkerObject else PouringWaterEmptyObject
            is PouringWaterMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) PouringWaterWaterObject() else PouringWaterEmptyObject
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 2/Puzzle Set 3/Pouring Water

        Summary
        Communicating Vessels

        Description
        1. The board represents some communicating vessels.
        2. You have to fill some water in it, considering that water pours down
           and levels itself like in reality.
        3. Areas of the same level which are horizontally connected will have
           the same water level.
        4. The numbers on the border show you how many tiles of each row and
           column are filled.
    */
    private fun updateIsSolved() {
        isSolved = true
    }
}