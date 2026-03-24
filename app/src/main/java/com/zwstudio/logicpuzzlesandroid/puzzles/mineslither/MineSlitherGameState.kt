package com.zwstudio.logicpuzzlesandroid.puzzles.mineslither

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MineSlitherGameState(game: MineSlitherGame) : CellsGameState<MineSlitherGame, MineSlitherGameMove, MineSlitherGameState>(game) {
    // https://stackoverflow.com/questions/43172947/kotlin-creating-a-mutable-list-with-repeating-elements
    var objArray = Array(rows * cols) { MineSlitherObject.Empty }
    var pos2state = mutableMapOf<Position, HintState>()

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MineSlitherObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MineSlitherObject) {this[p.row, p.col] = obj}

    override fun setObject(move: MineSlitherGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MineSlitherGameMove): GameOperationType {
        val p = move.p
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            MineSlitherObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MineSlitherObject.Marker else MineSlitherObject.Mine
            MineSlitherObject.Mine -> if (markerOption == MarkerOptions.MarkerLast) MineSlitherObject.Marker else MineSlitherObject.Empty
            MineSlitherObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MineSlitherObject.Mine else MineSlitherObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 3/MineSlither

        Summary
        MineSweeper, meet corners

        Description
        1. A number tells you how many mines are around that tile.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows)
            for (c in 0 until cols) {
                val p = Position(r, c)
                if (this[p] == MineSlitherObject.Forbidden)
                    this[p] = MineSlitherObject.Empty
            }
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MineSlitherGame.offset2) {
                val p2 = p + os
                if (!isValid(p2)) continue
                when (this[p2]) {
                    MineSlitherObject.Empty, MineSlitherObject.Marker -> rng.add(p2)
                    MineSlitherObject.Mine -> n1++
                    else -> {}
                }
            }
            // 1. A number tells you how many mines are around that tile.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete) isSolved = false
            if (s != HintState.Normal && allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = MineSlitherObject.Forbidden
        }
    }
}
