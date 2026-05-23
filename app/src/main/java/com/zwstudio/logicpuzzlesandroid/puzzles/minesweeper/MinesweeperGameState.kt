package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MinesweeperGameState(game: MinesweeperGame) : CellsGameState<MinesweeperGame, MinesweeperGameMove, MinesweeperGameState>(game) {
    val objArray = Array(rows * cols) { MinesweeperObject.Empty }
    val pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MinesweeperObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MinesweeperObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = MinesweeperObject.Hint
        updateIsSolved()
    }

    override fun setObject(move: MinesweeperGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == MinesweeperObject.Hint || this[p] == move.obj) return GameOperationType.Invalid
        this[p] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: MinesweeperGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == MinesweeperObject.Hint) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            MinesweeperObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) MinesweeperObject.Marker else MinesweeperObject.Mine
            MinesweeperObject.Mine -> if (markerOption == MarkerOptions.MarkerLast) MinesweeperObject.Marker else MinesweeperObject.Empty
            MinesweeperObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) MinesweeperObject.Mine else MinesweeperObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 6/Minesweeper

        Summary
        You know the drill :)

        Description
        1. Find the mines on the field.
        2. Numbers tell you how many mines there are close by, touching that
           number horizontally, vertically or diagonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == MinesweeperObject.Forbidden)
                    this[r, c] = MinesweeperObject.Empty
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MinesweeperGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o == MinesweeperObject.Mine)
                    n1++
                else if (o == MinesweeperObject.Empty)
                    rng.add(+p2)
            }
            // 2. Numbers tell you how many mines there are close by, touching that
            // number horizontally, vertically or diagonally.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            pos2state[p] = s
            if (s != HintState.Complete)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = MinesweeperObject.Forbidden
        }
    }
}