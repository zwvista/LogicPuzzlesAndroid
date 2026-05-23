package com.zwstudio.logicpuzzlesandroid.puzzles.pouringwater

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class PouringWaterGameState(game: PouringWaterGame) : CellsGameState<PouringWaterGame, PouringWaterGameMove, PouringWaterGameState>(game) {
    val objArray = Array(rows * cols) { PouringWaterObject.Empty }
    val row2state = Array(rows) { HintState.Normal }
    val col2state = Array(cols) { HintState.Normal }
    val pos2state = mutableMapOf<Position, AllowedObjectState>()

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
            PouringWaterObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) PouringWaterObject.Marker else PouringWaterObject.Water
            PouringWaterObject.Water -> if (markerOption == MarkerOptions.MarkerLast) PouringWaterObject.Marker else PouringWaterObject.Empty
            PouringWaterObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) PouringWaterObject.Water else PouringWaterObject.Empty
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
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == PouringWaterObject.Forbidden)
                    this[p] = PouringWaterObject.Empty
                pos2state[p] = AllowedObjectState.Normal
            }
        // 2. You have to fill some water in it, considering that water pours down
        //    and levels itself like in reality.
        // 3. Areas of the same level which are horizontally connected will have
        //    the same water level.
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val p = Position(r, c)
                if (this[p] == PouringWaterObject.Water && !listOf(1, 2, 3).all { i ->
                    game.dots[p + PouringWaterGame.offset2[i], PouringWaterGame.dirs[i]] == GridLineObject.Line ||
                            this[p + PouringWaterGame.offset[i]] == PouringWaterObject.Water
                }) { pos2state[p] = AllowedObjectState.Error; isSolved = false }
            }
        // 4. The numbers on the border show you how many tiles of each row and
        //    column are filled.
        for (r in 0..<rows) {
            val n2 = game.row2hint[r]
            if (n2 == PouringWaterGame.PUZ_UNKNOWN) continue
            val n1 = (0..<cols).count { this[r, it] == PouringWaterObject.Water }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0..<cols).filter { this[r, it] == PouringWaterObject.Empty }.forEach {
                    this[r, it] = PouringWaterObject.Forbidden
                }
        }
        for (c in 0..<cols) {
            val n2 = game.col2hint[c]
            if (n2 == PouringWaterGame.PUZ_UNKNOWN) continue
            val n1 = (0..<rows).count { this[it, c] == PouringWaterObject.Water }
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                (0..<rows).filter { this[it, c] == PouringWaterObject.Empty }.forEach {
                    this[it, c] = PouringWaterObject.Forbidden
                }
        }
    }
}