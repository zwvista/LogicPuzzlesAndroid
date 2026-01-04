package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import java.util.TreeMap

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
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is PouringWaterForbiddenObject)
                    this[r, c] = PouringWaterEmptyObject
        // 2. You have to fill some water in it, considering that water pours down
        //    and levels itself like in reality.
        // 3. Areas of the same level which are horizontally connected will have
        //    the same water level.
        for (area in game.areas) {
            val row2rng = TreeMap(area.groupBy { it.row })
            val rowNotFilled = row2rng.keys.reversed().firstOrNull {
                row2rng[it]!!.any { this[it] !is PouringWaterWaterObject }
            } ?: continue
            val rng = area.filter { this[it] is PouringWaterWaterObject }
            val rngError = rng.filter { it.row < rowNotFilled }
            rng.forEach { this[it] = PouringWaterWaterObject() }
            if (rngError.isEmpty()) continue
            isSolved = false
            rngError.forEach { this[it] = PouringWaterWaterObject(state = AllowedObjectState.Error) }
        }
        // 4. The numbers on the border show you how many tiles of each row and
        //    column are filled.
        for (r in 0 until rows) {
            val n2 = game.row2hint[r]
            if (n2 == PouringWaterGame.PUZ_UNKNOWN) continue
            val n1 = (0 until cols).count { this[r, it] is PouringWaterWaterObject }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until cols).filter { this[r, it] is PouringWaterEmptyObject }.forEach {
                    this[r, it] = PouringWaterForbiddenObject
                }
        }
        for (c in 0 until cols) {
            val n2 = game.col2hint[c]
            if (n2 == PouringWaterGame.PUZ_UNKNOWN) continue
            val n1 = (0 until rows).count { this[it, c] is PouringWaterWaterObject }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0 until rows).filter { this[it, c] is PouringWaterEmptyObject }.forEach {
                    this[it, c] = PouringWaterForbiddenObject
                }
        }
    }
}