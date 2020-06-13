package com.zwstudio.logicpuzzlesandroid.puzzles.minesweeper

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState

class MinesweeperGameState(game: MinesweeperGame) : CellsGameState<MinesweeperGame, MinesweeperGameMove, MinesweeperGameState>(game) {
    var objArray = Array<MinesweeperObject>(rows * cols) { MinesweeperEmptyObject() }
    var pos2state = mutableMapOf<Position, HintState>()

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: MinesweeperObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: MinesweeperObject) {this[p.row, p.col] = obj}

    init {
        for (p in game.pos2hint.keys)
            this[p] = MinesweeperHintObject()
        updateIsSolved()
    }

    fun setObject(move: MinesweeperGameMove): Boolean {
        if (this[move.p] == move.obj) return false
        this[move.p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: MinesweeperGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[move.p]
        move.obj = when (o) {
            is MinesweeperEmptyObject -> if (markerOption == MarkerOptions.MarkerFirst) MinesweeperMarkerObject() else MinesweeperMineObject()
            is MinesweeperMineObject -> if (markerOption == MarkerOptions.MarkerLast) MinesweeperMarkerObject() else MinesweeperEmptyObject()
            is MinesweeperMarkerObject -> if (markerOption == MarkerOptions.MarkerFirst) MinesweeperMineObject() else MinesweeperEmptyObject()
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
        for (r in 0 until rows)
            for (c in 0 until cols)
                if (this[r, c] is MinesweeperForbiddenObject)
                    this[r, c] = MinesweeperEmptyObject()
        for ((p, n2) in game.pos2hint) {
            var n1 = 0
            val rng = mutableListOf<Position>()
            for (os in MinesweeperGame.offset) {
                val p2 = p + os
                if (!isValid(p2)) continue
                val o = this[p2]
                if (o is MinesweeperMineObject)
                    n1++
                else if (o is MinesweeperEmptyObject)
                    rng.add(+p2)
            }
            // 2. Numbers tell you how many mines there are close by, touching that
            // number horizontally, vertically or diagonally.
            pos2state[p] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2)
                isSolved = false
            else if (allowedObjectsOnly)
                for (p2 in rng)
                    this[p2] = MinesweeperForbiddenObject()
        }
    }
}