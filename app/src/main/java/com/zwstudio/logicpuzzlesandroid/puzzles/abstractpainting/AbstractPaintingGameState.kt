package com.zwstudio.logicpuzzlesandroid.puzzles.abstractpainting

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.GameOperationType
import com.zwstudio.logicpuzzlesandroid.common.domain.HintState
import com.zwstudio.logicpuzzlesandroid.common.domain.MarkerOptions
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class AbstractPaintingGameState(game: AbstractPaintingGame) : CellsGameState<AbstractPaintingGame, AbstractPaintingGameMove, AbstractPaintingGameState>(game) {
    val objArray = Array(rows * cols) { AbstractPaintingObject.Empty }
    val row2state = Array(rows) { HintState.Normal }
    val col2state = Array(cols) { HintState.Normal }

    init {
        updateIsSolved()
    }

    operator fun get(row: Int, col: Int) = objArray[row * cols + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: AbstractPaintingObject) {objArray[row * cols + col] = obj}
    operator fun set(p: Position, obj: AbstractPaintingObject) {this[p.row, p.col] = obj}

    override fun setObject(move: AbstractPaintingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return GameOperationType.Invalid
        // 3. The region of the painting can be entirely hidden or revealed.
        for (p2 in game.areas[game.pos2area[p]!!])
            this[p2] = move.obj
        updateIsSolved()
        return GameOperationType.MoveComplete
    }

    override fun switchObject(move: AbstractPaintingGameMove): GameOperationType {
        val p = move.p
        if (!isValid(p)) return GameOperationType.Invalid
        val markerOption = MarkerOptions.entries[game.gdi.markerOption]
        move.obj = when (val o = this[p]) {
            AbstractPaintingObject.Empty -> if (markerOption == MarkerOptions.MarkerFirst) AbstractPaintingObject.Marker else AbstractPaintingObject.Painting
            AbstractPaintingObject.Painting -> if (markerOption == MarkerOptions.MarkerLast) AbstractPaintingObject.Marker else AbstractPaintingObject.Empty
            AbstractPaintingObject.Marker -> if (markerOption == MarkerOptions.MarkerFirst) AbstractPaintingObject.Painting else AbstractPaintingObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: 100 Logic Games 4/Puzzle Set 4/Abstract Mirror Painting

        Summary
        Aliens, move over, the Next Trend is here!

        Description
        1. Diagonal mirrors are out, the new trend is orthogonal mirror abstract painting!
        2. You should paint areas that span two adjacent regions. The area is symmetrical with respect
           to the regions border.
        3. Numbers tell you how many tiles in that region are painted.
        4. Areas can't touch orthogonally.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0..<rows)
            for (c in 0..<cols)
                if (this[r, c] == AbstractPaintingObject.Forbidden)
                    this[r, c] = AbstractPaintingObject.Empty
        for (r in 0..<rows) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0..<cols)
                if (this[r, c] == AbstractPaintingObject.Painting)
                    n1++
            // 2. Outer numbers tell how many tiles form the painting on the row.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            row2state[r] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (c in 0..<cols) {
            var n1 = 0
            val n2 = game.col2hint[c]
            for (r in 0..<rows)
                if (this[r, c] == AbstractPaintingObject.Painting)
                    n1++
            // 2. Outer numbers tell how many tiles form the painting on the column.
            val s = if (n1 < n2) HintState.Normal else if (n1 == n2 || n2 == -1) HintState.Complete else HintState.Error
            col2state[c] = s
            if (s != HintState.Complete) isSolved = false
        }
        for (r in 0..<rows)
            for (c in 0..<cols) {
                val o = this[r, c]
                if ((o == AbstractPaintingObject.Empty || o == AbstractPaintingObject.Marker) &&
                        allowedObjectsOnly && (row2state[r] != HintState.Normal && game.row2hint[r] != -1 ||
                        col2state[c] != HintState.Normal && game.col2hint[c] != -1))
                    this[r, c] = AbstractPaintingObject.Forbidden
            }
    }
}
