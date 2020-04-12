package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class AbstractPaintingGameState(game: AbstractPaintingGame) : CellsGameState<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState>(game) {
    var objArray = Array(rows() * cols()) { AbstractPaintingObject.Empty }
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: AbstractPaintingObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: AbstractPaintingObject) {this[p.row, p.col] = obj}

    fun setObject(move: AbstractPaintingGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        // 3. The region of the painting can be entirely hidden or revealed.
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: AbstractPaintingGameMove): Boolean {
        val p = move.p
        if (!isValid(p)) return false
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val o = this[p]
        move.obj = when (o) {
            AbstractPaintingObject.Empty ->
                if (markerOption == MarkerOptions.MarkerFirst) AbstractPaintingObject.Marker else AbstractPaintingObject.Painting
            AbstractPaintingObject.Painting ->
                if (markerOption == MarkerOptions.MarkerLast) AbstractPaintingObject.Marker else AbstractPaintingObject.Empty
            AbstractPaintingObject.Marker ->
                if (markerOption == MarkerOptions.MarkerFirst) AbstractPaintingObject.Painting else AbstractPaintingObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 16/Abstract Painting

        Summary
        Abstract Logic

        Description
        1. The goal is to reveal part of the abstract painting behind the board.
        2. Outer numbers tell how many tiles form the painting on the row and column.
        3. The region of the painting can be entirely hidden or revealed.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] == AbstractPaintingObject.Forbidden)
                    this[r, c] = AbstractPaintingObject.Empty
        for (r in 0 until rows()) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols())
                if (this[r, c] == AbstractPaintingObject.Painting)
                    n1++
            // 2. Outer numbers tell how many tiles form the painting on the row.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 0 until cols()) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0 until rows())
                if (this[r, c] == AbstractPaintingObject.Painting)
                    n1++
            // 2. Outer numbers tell how many tiles form the painting on the column.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if ((o == AbstractPaintingObject.Empty || o == AbstractPaintingObject.Marker) &&
                        allowedObjectsOnly && (row2state[r] != HintState.Normal && game.row2hint[r] != -1 ||
                        col2state[c] != HintState.Normal && game.col2hint[c] != -1))
                    this[r, c] = AbstractPaintingObject.Forbidden
            }
    }
}
